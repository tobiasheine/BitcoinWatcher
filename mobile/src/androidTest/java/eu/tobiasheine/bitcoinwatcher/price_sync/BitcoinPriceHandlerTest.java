package eu.tobiasheine.bitcoinwatcher.price_sync;

import android.test.AndroidTestCase;

import eu.tobiasheine.bitcoinwatcher.models.BitcoinBpi;
import eu.tobiasheine.bitcoinwatcher.models.BitcoinPrice;
import eu.tobiasheine.bitcoinwatcher.models.BitcoinPriceBpi;
import eu.tobiasheine.bitcoinwatcher.storage.Storage;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.Notifications;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BitcoinPriceHandlerTest extends AndroidTestCase {

    private Storage storage;
    private Notifications notifications;
    private Settings settings;

    private BitcoinPriceHandler priceUpdater;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // http://stackoverflow.com/questions/12267572/mockito-dexmaker-on-android
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());

        storage = mock(Storage.class);
        notifications = mock(Notifications.class);
        settings = mock(Settings.class);

        priceUpdater = new BitcoinPriceHandler(storage, notifications, settings);
    }

    public void testStoresNewPriceOnUpdate() throws Exception {
        // given
        final BitcoinPrice newPrice = mock(BitcoinPrice.class);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(storage).storeNewCurrentPrice(newPrice);
    }

    public void testNotifyWidgetOnUpdate() throws Exception {
        // given
        final BitcoinPrice newPrice = mock(BitcoinPrice.class);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(notifications).notifyWidget();

    }

    public void testNotifyWhenPriceChangeEqualsLimit() throws Exception {
        // given
        final int priceLimit = 10;
        final float lastRate = 150f;
        when(settings.getPriceChangeLimitInPercentage()).thenReturn(priceLimit);

        final BitcoinPrice lastPrice = getCurrentPrice(lastRate);
        final BitcoinPrice newPrice = getCurrentPrice(lastRate + lastRate / 100 * priceLimit);

        when(storage.getLatestCurrentPrice()).thenReturn(lastPrice);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(notifications).notifyAboutNewPrice(newPrice);
    }

    public void testNotifyWhenPriceChangeIsPositiveAndMoreThanLimit() throws Exception {
        // given
        final int priceLimit = 10;
        final float lastRate = 150f;
        when(settings.getPriceChangeLimitInPercentage()).thenReturn(priceLimit);

        final BitcoinPrice lastPrice = getCurrentPrice(lastRate);
        final BitcoinPrice newPrice = getCurrentPrice((lastRate + lastRate / 100 * priceLimit) + 10);

        when(storage.getLatestCurrentPrice()).thenReturn(lastPrice);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(notifications).notifyAboutNewPrice(newPrice);
    }

    public void testNotifyWhenPriceChangeIsNegativeAndMoreThanLimit() throws Exception {
        // given
        final int priceLimit = 10;
        final float lastRate = 150f;
        when(settings.getPriceChangeLimitInPercentage()).thenReturn(priceLimit);

        final BitcoinPrice lastPrice = getCurrentPrice(lastRate);
        final BitcoinPrice newPrice = getCurrentPrice((lastRate - lastRate / 100 * priceLimit) - 10);

        when(storage.getLatestCurrentPrice()).thenReturn(lastPrice);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(notifications).notifyAboutNewPrice(newPrice);
    }

    public void testDoNotNotifiyWhenPriceChangeIsBelowLimit() throws Exception {
        // given
        final int priceLimit = 10;
        final float lastRate = 150f;
        when(settings.getPriceChangeLimitInPercentage()).thenReturn(priceLimit);

        final BitcoinPrice lastPrice = getCurrentPrice(lastRate);
        final BitcoinPrice newPrice = getCurrentPrice((lastRate + lastRate / 100 * priceLimit) - 10);

        when(storage.getLatestCurrentPrice()).thenReturn(lastPrice);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(notifications, never()).notifyAboutNewPrice(newPrice);

    }

    private BitcoinPrice getCurrentPrice(float lastRate) {
        final BitcoinPrice lastPrice = mock(BitcoinPrice.class);
        final BitcoinPriceBpi lastPriceBpi = mock(BitcoinPriceBpi.class);
        final BitcoinBpi lastPriceBitcoin = new BitcoinBpi();

        when(lastPrice.getBpi()).thenReturn(lastPriceBpi);
        when(lastPriceBpi.getEur()).thenReturn(lastPriceBitcoin);
        lastPriceBitcoin.rate_float = lastRate;

        return lastPrice;
    }
}