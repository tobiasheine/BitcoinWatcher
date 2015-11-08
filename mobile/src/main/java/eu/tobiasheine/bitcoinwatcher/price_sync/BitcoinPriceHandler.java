package eu.tobiasheine.bitcoinwatcher.price_sync;

import javax.inject.Inject;

import eu.tobiasheine.bitcoinwatcher.BitcoinWatcherApplication;
import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;
import eu.tobiasheine.bitcoinwatcher.dao.storage.IStorage;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IHandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IWearableNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.WearableNotifications;
import eu.tobiasheine.bitcoinwatcher.dao.storage.Storage;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.HandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;

public class BitcoinPriceHandler {

    @Inject
    IStorage storage;
    @Inject
    ISettings settings;
    @Inject
    IHandheldNotifications handheldNotifications;
    @Inject
    IWearableNotifications wearableNotifications;

    public BitcoinPriceHandler() {
        BitcoinWatcherApplication.getDependencies().inject(this);
    }

    public void handleNewBitcoinPrice(final BitcoinPriceDTO newBitcoinPriceDTO) {
        final BitcoinPriceDTO latestBitcoinPriceDTO = storage.getStoredPrice();

        storage.storeNewPrice(newBitcoinPriceDTO);

        handheldNotifications.notifyWidget();
        wearableNotifications.notifyWearable();

        if (latestBitcoinPriceDTO != null && shouldNotifyAboutPrice(latestBitcoinPriceDTO, newBitcoinPriceDTO)) {
            handheldNotifications.notifyAboutNewPrice(newBitcoinPriceDTO);
        }
    }

    private boolean shouldNotifyAboutPrice(final BitcoinPriceDTO latestBitcoinPriceDTO, final BitcoinPriceDTO newBitcoinPriceDTO) {
        final float lastRate = latestBitcoinPriceDTO.getBpi().getEur().rate_float;
        final float newRate = newBitcoinPriceDTO.getBpi().getEur().rate_float;

        final float priceChange = Math.abs(newRate - lastRate);
        final int priceChangeLimitInPercentage = settings.getPriceChangeLimitInPercentage();

        return priceChange >= lastRate / 100 * priceChangeLimitInPercentage;
    }
}
