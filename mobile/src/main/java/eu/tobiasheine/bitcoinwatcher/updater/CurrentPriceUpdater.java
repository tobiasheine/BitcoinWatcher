package eu.tobiasheine.bitcoinwatcher.updater;

import eu.tobiasheine.bitcoinwatcher.model.CurrentPrice;
import eu.tobiasheine.bitcoinwatcher.notifier.CurrentPriceNotifier;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;
import eu.tobiasheine.bitcoinwatcher.storage.CurrentPriceStorage;

public class CurrentPriceUpdater {

    private final CurrentPriceStorage storage;
    private final CurrentPriceNotifier notifier;
    private final Settings settings;

    public CurrentPriceUpdater(CurrentPriceStorage storage, CurrentPriceNotifier notifier, Settings settings) {
        this.storage = storage;
        this.notifier = notifier;
        this.settings = settings;
    }

    public void updateCurrentPrice(final CurrentPrice newCurrentPrice) {
        final CurrentPrice latestCurrentPrice = storage.getLatestCurrentPrice();

        storage.storeNewCurrentPrice(newCurrentPrice);

        if (latestCurrentPrice != null && shouldNotifyAboutPrice(latestCurrentPrice, newCurrentPrice)) {
            notifier.notifyAboutNewCurrentPrice(newCurrentPrice);
        }
    }

    private boolean shouldNotifyAboutPrice(final CurrentPrice latestCurrentPrice, final CurrentPrice newCurrentPrice) {
        final float lastRate = latestCurrentPrice.getBpi().getEur().rate_float;
        final float newRate = newCurrentPrice.getBpi().getEur().rate_float;

        final float priceChange = Math.abs(newRate - lastRate);
        final int priceChangeLimitInPercentage = settings.getPriceChangeLimitInPercentage();

        return priceChange >= lastRate / 100 * priceChangeLimitInPercentage;
    }
}
