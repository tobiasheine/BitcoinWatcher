package eu.tobiasheine.bitcoinwatcher.price_sync;

import org.junit.Before;
import org.junit.Test;

import eu.tobiasheine.bitcoinwatcher.BitcoinWatcherApplication;
import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinBpiDTO;
import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceBpiDTO;
import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;
import eu.tobiasheine.bitcoinwatcher.dao.storage.IStorage;
import eu.tobiasheine.bitcoinwatcher.dao.storage.Storage;
import eu.tobiasheine.bitcoinwatcher.di.UtsDependencies;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.HandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IHandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IWearableNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.WearableNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TheBitcoinPriceHandler {

    private IStorage storage;
    private IHandheldNotifications handheldNotifications;
    private ISettings settings;
    private IWearableNotifications wearableNotifications;

    private BitcoinPriceHandler priceUpdater;

    @Before
    public void setUp() throws Exception {
        final UtsDependencies dependencies = new UtsDependencies();
        BitcoinWatcherApplication.replaceDependencies(dependencies);

        storage = mock(Storage.class);
        handheldNotifications = mock(IHandheldNotifications.class);
        wearableNotifications = mock(IWearableNotifications.class);
        settings = mock(ISettings.class);
        dependencies.replace(IStorage.class, storage);
        dependencies.replace(IHandheldNotifications.class, handheldNotifications);
        dependencies.replace(IWearableNotifications.class, wearableNotifications);
        dependencies.replace(ISettings.class, settings);

        priceUpdater = new BitcoinPriceHandler();
    }

    @Test
    public void storesNewPriceOnUpdate() throws Exception {
        // given
        final BitcoinPriceDTO newPrice = mock(BitcoinPriceDTO.class);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(storage).storeNewPrice(newPrice);
    }

    @Test
    public void notifyWidgetAndWearableOnUpdate() throws Exception {
        // given
        final BitcoinPriceDTO newPrice = mock(BitcoinPriceDTO.class);

        // when
        priceUpdater.handleNewBitcoinPrice(newPrice);

        // then
        verify(handheldNotifications).notifyWidget();
        verify(wearableNotifications).notifyWearable();
    }

    @Test
    public void notifyWhenPriceChangeEqualsLimit() throws Exception {
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

    @Test
    public void notifyWhenPriceChangeIsPositiveAndMoreThanLimit() throws Exception {
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

    public void notifyWhenPriceChangeIsNegativeAndMoreThanLimit() throws Exception {
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

    @Test
    public void doNotNotifiyWhenPriceChangeIsBelowLimit() throws Exception {
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