package eu.tobiasheine.bitcoinwatcher.price_sync.notifications;

import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;
import eu.tobiasheine.bitcoinwatcher.core.PriceConverter;
import eu.tobiasheine.bitcoinwatcher.core.domain.BitcoinPrice;
import eu.tobiasheine.bitcoinwatcher.core.domain.Currency;
import eu.tobiasheine.bitcoinwatcher.dao.storage.IStorage;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;
import eu.tobiasheine.bitcoinwatcher.dao.storage.Storage;

public class WearableNotifications implements IWearableNotifications {

    private final PriceConverter converter;
    private final Runnable notifyingRunnable;
    private final GoogleApiClient googleApiClient;

    @Inject
    public WearableNotifications(final GoogleApiClient googleApiClient, final IStorage storage, final ISettings settings) {
        this.converter = new PriceConverter();
        this.googleApiClient = googleApiClient;

        this.notifyingRunnable = new Runnable() {
            @Override
            public void run() {

                final BitcoinPrice bitcoinPrice = getBitcoinPrice(settings, storage);

                try {
                    final byte[] priceInBytes = converter.convertToByte(bitcoinPrice);

                    PutDataRequest priceDataMap = PutDataRequest.create("/price");
                    priceDataMap.setData(priceInBytes);

                    Wearable.DataApi.putDataItem(googleApiClient, priceDataMap);

                } catch (IOException e) {
                    Log.e("WearableNotifications", Log.getStackTraceString(e));
                }
            }
        };
    }

    private BitcoinPrice getBitcoinPrice(ISettings settings, IStorage storage) {
        Currency selectedCurrency = settings.getSelectedCurrency();

        float rate = -1f;

        BitcoinPriceDTO storedPrice = storage.getStoredPrice();
        switch (selectedCurrency) {
            case EUR:
                rate = storedPrice.getBpi().getEur().rate_float;
                break;

            case USD:
                rate = storedPrice.getBpi().getUsd().rate_float;
                break;

            case GBP:
                rate = storedPrice.getBpi().getGbp().rate_float;
                break;
        }

        return new BitcoinPrice(selectedCurrency, rate, storedPrice.getTime().getUpdatedISO());
    }

    @Override
    public void notifyWearable() {
        if ((Looper.myLooper() == Looper.getMainLooper())) {
            new Thread(notifyingRunnable).start();
        } else {
          notifyingRunnable.run();
        }
    }

    @Override
    public void connect() {
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    @Override
    public void disconnect() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

}
