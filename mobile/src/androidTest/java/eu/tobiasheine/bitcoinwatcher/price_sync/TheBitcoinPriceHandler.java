package eu.tobiasheine.bitcoinwatcher.price_sync;

import android.test.AndroidTestCase;

import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinBpiDTO;
import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceBpiDTO;
import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.WearableNotifications;
import eu.tobiasheine.bitcoinwatcher.dao.storage.Storage;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.HandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TheBitcoinPriceHandler extends AndroidTestCase {

    private Storage storage;
    private HandheldNotifications handheldNotifications;
    private Settings settings;
    private WearableNotifications wearableNotifications;

    private BitcoinPriceHandler priceUpdater;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        storage = mock(Storage.class);
        handheldNotifications = mock(HandheldNotifications.class);
        wearableNotifications = mock(WearableNotifications.class);
        settings = mock(Settings.class);

        priceUpdater = new BitcoinPriceHandler(storage, handheldNotifications, settings, wearableNotifications);
    }

    public void testStoresNewPriceOnUpdate() throws Exception {
        // given
        final BitcoinPriceDTO newPrice = mock(BitcoinPriceDTO.class);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(storage).storeNewPrice(newPrice);
    }

    public void testNotifyWidgetAndWearableOnUpdate() throws Exception {
        // given
        final BitcoinPriceDTO newPrice = mock(BitcoinPriceDTO.class);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(handheldNotifications).notifyWidget();
        verify(wearableNotifications).notifyWearable();
    }

    public void testNotifyWhenPriceChangeEqualsLimit() throws Exception {
        // given
        final int priceLimit = 10;
        final float lastRate = 150f;
        when(settings.getPriceChangeLimitInPercentage()).thenReturn(priceLimit);

        final BitcoinPriceDTO lastPrice = getCurrentPrice(lastRate);
        final BitcoinPriceDTO newPrice = getCurrentPrice(lastRate + lastRate / 100 * priceLimit);

        when(storage.getStoredPrice()).thenReturn(lastPrice);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(handheldNotifications).notifyAboutNewPrice(newPrice);
    }

    public void testNotifyWhenPriceChangeIsPositiveAndMoreThanLimit() throws Exception {
        // given
        final int priceLimit = 10;
        final float lastRate = 150f;
        when(settings.getPriceChangeLimitInPercentage()).thenReturn(priceLimit);

        final BitcoinPriceDTO lastPrice = getCurrentPrice(lastRate);
        final BitcoinPriceDTO newPrice = getCurrentPrice((lastRate + lastRate / 100 * priceLimit) + 10);

        when(storage.getStoredPrice()).thenReturn(lastPrice);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(handheldNotifications).notifyAboutNewPrice(newPrice);
    }

    public void testNotifyWhenPriceChangeIsNegativeAndMoreThanLimit() throws Exception {
        // given
        final int priceLimit = 10;
        final float lastRate = 150f;
        when(settings.getPriceChangeLimitInPercentage()).thenReturn(priceLimit);

        final BitcoinPriceDTO lastPrice = getCurrentPrice(lastRate);
        final BitcoinPriceDTO newPrice = getCurrentPrice((lastRate - lastRate / 100 * priceLimit) - 10);

        when(storage.getStoredPrice()).thenReturn(lastPrice);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(handheldNotifications).notifyAboutNewPrice(newPrice);
    }

    public void testDoNotNotifiyWhenPriceChangeIsBelowLimit() throws Exception {
        // given
        final int priceLimit = 10;
        final float lastRate = 150f;
        when(settings.getPriceChangeLimitInPercentage()).thenReturn(priceLimit);

        final BitcoinPriceDTO lastPrice = getCurrentPrice(lastRate);
        final BitcoinPriceDTO newPrice = getCurrentPrice((lastRate + lastRate / 100 * priceLimit) - 10);

        when(storage.getStoredPrice()).thenReturn(lastPrice);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(handheldNotifications, never()).notifyAboutNewPrice(newPrice);

    }

    private BitcoinPriceDTO getCurrentPrice(float lastRate) {
        final BitcoinPriceDTO lastPrice = mock(BitcoinPriceDTO.class);
        final BitcoinPriceBpiDTO lastPriceBpi = mock(BitcoinPriceBpiDTO.class);
        final BitcoinBpiDTO lastPriceBitcoin = new BitcoinBpiDTO();

        when(lastPrice.getBpi()).thenReturn(lastPriceBpi);
        when(lastPriceBpi.getEur()).thenReturn(lastPriceBitcoin);
        lastPriceBitcoin.rate_float = lastRate;

        return lastPrice;
    }
}