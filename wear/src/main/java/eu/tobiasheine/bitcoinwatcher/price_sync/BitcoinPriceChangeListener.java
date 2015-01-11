package eu.tobiasheine.bitcoinwatcher.price_sync;

import android.util.Log;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.IOException;
import java.util.List;

import eu.tobiasheine.bitcoinwatcher.core.PriceConverter;
import eu.tobiasheine.bitcoinwatcher.core.domain.BitcoinPrice;

public class BitcoinPriceChangeListener extends WearableListenerService {

    private Storage storage;
    private PriceConverter converter;

    @Override
    public void onCreate() {
        super.onCreate();
        storage = new Storage(getBaseContext());
        converter = new PriceConverter();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);

        for (final DataEvent event : events) {
            final BitcoinPrice bitcoinPrice = getBitcoinPriceFromDataEvent(event);
            storage.storePrice(bitcoinPrice);
        }
    }

    private BitcoinPrice getBitcoinPriceFromDataEvent(final DataEvent dataEvent) {
        try {
            return converter.convertToPrice(dataEvent.getDataItem().getData());
        } catch (IOException | ClassNotFoundException e) {
            Log.e("BitcoinPriceListener", Log.getStackTraceString(e));
        }

        return null;
    }
}
