package eu.tobiasheine.bitcoinwatcher.price_sync;

import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.WearableNotifications;
import eu.tobiasheine.bitcoinwatcher.dao.storage.Storage;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.HandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;

public class BitcoinPriceHandler {

    private final Storage storage;
    private final Settings settings;
    private final HandheldNotifications handheldNotifications;
    private final WearableNotifications wearableNotifications;

    public BitcoinPriceHandler(final Storage storage, final HandheldNotifications handheldNotifications, final Settings settings, final WearableNotifications wearableNotifications) {
        this.storage = storage;
        this.handheldNotifications = handheldNotifications;
        this.settings = settings;
        this.wearableNotifications = wearableNotifications;
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
