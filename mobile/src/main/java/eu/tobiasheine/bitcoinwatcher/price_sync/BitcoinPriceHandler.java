package eu.tobiasheine.bitcoinwatcher.price_sync;

import eu.tobiasheine.bitcoinwatcher.models.BitcoinPrice;
import eu.tobiasheine.bitcoinwatcher.storage.Storage;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.Notifications;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;

public class BitcoinPriceHandler {

    private final Storage storage;
    private final Notifications notifications;
    private final Settings settings;

    public BitcoinPriceHandler(Storage storage, Notifications notifications, Settings settings) {
        this.storage = storage;
        this.notifications = notifications;
        this.settings = settings;
    }

    public void handleNewBitcoinPrice(final BitcoinPrice newBitcoinPrice) {
        final BitcoinPrice latestBitcoinPrice = storage.getLatestCurrentPrice();

        storage.storeNewCurrentPrice(newBitcoinPrice);

        notifications.notifyWidget();

        if (latestBitcoinPrice != null && shouldNotifyAboutPrice(latestBitcoinPrice, newBitcoinPrice)) {
            notifications.notifyAboutNewPrice(newBitcoinPrice);
        }
    }

    private boolean shouldNotifyAboutPrice(final BitcoinPrice latestBitcoinPrice, final BitcoinPrice newBitcoinPrice) {
        final float lastRate = latestBitcoinPrice.getBpi().getEur().rate_float;
        final float newRate = newBitcoinPrice.getBpi().getEur().rate_float;

        final float priceChange = Math.abs(newRate - lastRate);
        final int priceChangeLimitInPercentage = settings.getPriceChangeLimitInPercentage();

        return priceChange >= lastRate / 100 * priceChangeLimitInPercentage;
    }
}
