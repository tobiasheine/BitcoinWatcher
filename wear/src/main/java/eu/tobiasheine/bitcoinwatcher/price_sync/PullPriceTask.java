package eu.tobiasheine.bitcoinwatcher.price_sync;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;

import eu.tobiasheine.bitcoinwatcher.core.PriceConverter;
import eu.tobiasheine.bitcoinwatcher.core.domain.BitcoinPrice;

public class PullPriceTask extends AsyncTask<Void, Void, BitcoinPrice> {

    private final GoogleApiClient googleApiClient;
    private final Storage storage;
    private final PriceConverter priceConverter;

    public PullPriceTask(GoogleApiClient googleApiClient, Storage storage) {
        this.googleApiClient = googleApiClient;
        this.storage = storage;
        this.priceConverter = new PriceConverter();
    }

    @Override
    protected BitcoinPrice doInBackground(Void... params) {
        if (!googleApiClient.isConnected()) {
            Log.e("PullPrice", "Cannot pull price cause GoogleApiClient is not connected");
            return null;
        }

        final DataItemBuffer itemBuffer = Wearable.DataApi.getDataItems(googleApiClient).await();

        if (!(itemBuffer.getCount() > 0)) {
            Log.e("PullPrice", "Could not fetch any DataItems");
            return null;
        }

        final  DataItem dataItem = itemBuffer.get(0);

        try {
            return priceConverter.convertToPrice(dataItem.getData());
        } catch (IOException | ClassNotFoundException e) {
            Log.e("PullPrice", "Cannot convert DataItem to BitcoinPrice");
            return null;
        }
    }

    @Override
    protected void onPostExecute(final BitcoinPrice bitcoinPrice) {
        super.onPostExecute(bitcoinPrice);

        if (bitcoinPrice != null) {
            storage.storePrice(bitcoinPrice);
        }
    }
}
