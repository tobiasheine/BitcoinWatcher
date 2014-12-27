package eu.tobiasheine.bitcoinwatcher.updater;

import eu.tobiasheine.bitcoinwatcher.model.CurrentPrice;
import eu.tobiasheine.bitcoinwatcher.notifier.CurrentPriceNotifier;
import eu.tobiasheine.bitcoinwatcher.storage.CurrentPriceStorage;

public class CurrentPriceUpdater {

    private final CurrentPriceStorage storage;
    private final CurrentPriceNotifier notifier;

    public CurrentPriceUpdater(CurrentPriceStorage storage, CurrentPriceNotifier notifier) {
        this.storage = storage;
        this.notifier = notifier;
    }

    public void updateCurrentPrice(final CurrentPrice newCurrentPrice) {
        final CurrentPrice latestCurrentPrice = storage.getLatestCurrentPrice();

        storage.storeNewCurrentPrice(newCurrentPrice);

        if (latestCurrentPrice != null && shouldNotifyAboutPrice(latestCurrentPrice, newCurrentPrice)) {
            notifier.notifyAboutNewCurrentPrice(newCurrentPrice);
        }
    }

    private boolean shouldNotifyAboutPrice(final CurrentPrice latestCurrentPrice, final CurrentPrice newCurrentPrice) {
        return true;
    }
}
