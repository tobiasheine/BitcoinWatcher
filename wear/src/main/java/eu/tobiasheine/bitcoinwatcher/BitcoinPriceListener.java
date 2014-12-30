package eu.tobiasheine.bitcoinwatcher;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.IOException;

import eu.tobiasheine.bitcoinwatcher.core.PriceConverter;
import eu.tobiasheine.bitcoinwatcher.core.domain.BitcoinPrice;

public class BitcoinPriceListener extends WearableListenerService {

    private Storage storage;
    private PriceConverter converter;

    @Override
    public void onCreate() {
        super.onCreate();
        storage = new Storage(getBaseContext());
        converter = new PriceConverter();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        final BitcoinPrice bitcoinPrice = getBitcoinPriceFromMessage(messageEvent);
        storage.storePrice(bitcoinPrice);
    }

    private BitcoinPrice getBitcoinPriceFromMessage(MessageEvent messageEvent) {
        try {
            return converter.convertToPrice(messageEvent.getData());
        } catch (IOException | ClassNotFoundException e) {
            Log.e("BitcoinPriceListener", Log.getStackTraceString(e));
        }

        return null;
    }
}
