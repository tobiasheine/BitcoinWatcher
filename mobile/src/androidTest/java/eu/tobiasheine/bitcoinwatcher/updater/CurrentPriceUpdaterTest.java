package eu.tobiasheine.bitcoinwatcher.updater;

import android.test.AndroidTestCase;

import eu.tobiasheine.bitcoinwatcher.model.BitcoinBpi;
import eu.tobiasheine.bitcoinwatcher.model.CurrentPrice;
import eu.tobiasheine.bitcoinwatcher.model.CurrentPriceBpi;
import eu.tobiasheine.bitcoinwatcher.notifier.CurrentPriceNotifier;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;
import eu.tobiasheine.bitcoinwatcher.storage.CurrentPriceStorage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CurrentPriceUpdaterTest extends AndroidTestCase {

    private CurrentPriceStorage storage;
    private CurrentPriceNotifier notifier;
    private Settings settings;

    private CurrentPriceUpdater priceUpdater;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // http://stackoverflow.com/questions/12267572/mockito-dexmaker-on-android
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());

        storage = mock(CurrentPriceStorage.class);
        notifier = mock(CurrentPriceNotifier.class);
        settings = mock(Settings.class);

        priceUpdater = new CurrentPriceUpdater(storage, notifier, settings);
    }

    public void testStoresNewPriceOnUpdate() throws Exception {
        // given
        final CurrentPrice newPrice = mock(CurrentPrice.class);

        // when
        priceUpdater.updateCurrentPrice(newPrice);

        // then
        verify(storage).storeNewCurrentPrice(newPrice);
    }

    public void testNotifyWhenPriceChangeEqualsLimit() throws Exception {
        // given
        int priceLimit = 10;
        final float lastRate = 150f;
        when(settings.getPriceChangeLimitInPercentage()).thenReturn(priceLimit);

        final CurrentPrice lastPrice = getCurrentPrice(lastRate);
        final CurrentPrice newPrice = getCurrentPrice(lastRate + lastRate / 100 * priceLimit);

        when(storage.getLatestCurrentPrice()).thenReturn(lastPrice);

        // when
        priceUpdater.updateCurrentPrice(newPrice);

        // then
        verify(notifier).notifyAboutNewCurrentPrice(newPrice);
    }

    public void testNotifyWhenPriceChangeIsPositiveAndMoreThanLimit() throws Exception {
        // given
        int priceLimit = 10;
        final float lastRate = 150f;
        when(settings.getPriceChangeLimitInPercentage()).thenReturn(priceLimit);

        final CurrentPrice lastPrice = getCurrentPrice(lastRate);
        final CurrentPrice newPrice = getCurrentPrice((lastRate + lastRate / 100 * priceLimit) + 10);

        when(storage.getLatestCurrentPrice()).thenReturn(lastPrice);

        // when
        priceUpdater.updateCurrentPrice(newPrice);

        // then
        verify(notifier).notifyAboutNewCurrentPrice(newPrice);
    }

    public void testNotifyWhenPriceChangeIsNegativeAndMoreThanLimit() throws Exception {
        // given
        int priceLimit = 10;
        final float lastRate = 150f;
        when(settings.getPriceChangeLimitInPercentage()).thenReturn(priceLimit);

        final CurrentPrice lastPrice = getCurrentPrice(lastRate);
        final CurrentPrice newPrice = getCurrentPrice((lastRate - lastRate / 100 * priceLimit) - 10);

        when(storage.getLatestCurrentPrice()).thenReturn(lastPrice);

        // when
        priceUpdater.updateCurrentPrice(newPrice);

        // then
        verify(notifier).notifyAboutNewCurrentPrice(newPrice);
    }

    public void testDoNotNotifiyWhenPriceChangeIsBelowLimit() throws Exception {
        // given
        int priceLimit = 10;
        final float lastRate = 150f;
        when(settings.getPriceChangeLimitInPercentage()).thenReturn(priceLimit);

        final CurrentPrice lastPrice = getCurrentPrice(lastRate);
        final CurrentPrice newPrice = getCurrentPrice((lastRate + lastRate / 100 * priceLimit) - 10);

        when(storage.getLatestCurrentPrice()).thenReturn(lastPrice);

        // when
        priceUpdater.updateCurrentPrice(newPrice);

        // then
        verifyZeroInteractions(notifier);

    }

    private CurrentPrice getCurrentPrice(float lastRate) {
        final CurrentPrice lastPrice = mock(CurrentPrice.class);
        final CurrentPriceBpi lastPriceBpi = mock(CurrentPriceBpi.class);
        final BitcoinBpi lastPriceBitcoin = new BitcoinBpi();

        when(lastPrice.getBpi()).thenReturn(lastPriceBpi);
        when(lastPriceBpi.getEur()).thenReturn(lastPriceBitcoin);
        lastPriceBitcoin.rate_float = lastRate;

        return lastPrice;
    }
}