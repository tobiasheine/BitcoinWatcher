package eu.tobiasheine.bitcoinwatcher.price_sync;

import javax.inject.Inject;

import eu.tobiasheine.bitcoinwatcher.BitcoinWatcherApplication;
import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;
import eu.tobiasheine.bitcoinwatcher.dao.storage.IStorage;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IHandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IWearableNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;

public class BitcoinPriceHandler implements IBitcoinPriceHandler {

    private final IStorage storage;
    private final ISettings settings;
    private final IHandheldNotifications handheldNotifications;
    private final IWearableNotifications wearableNotifications;

    @Inject
    public BitcoinPriceHandler(IStorage storage, ISettings settings, IHandheldNotifications handheldNotifications, IWearableNotifications wearableNotifications) {
        this.storage = storage;
        this.settings = settings;
        this.handheldNotifications = handheldNotifications;
        this.wearableNotifications = wearableNotifications;
    }


    @Override
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
