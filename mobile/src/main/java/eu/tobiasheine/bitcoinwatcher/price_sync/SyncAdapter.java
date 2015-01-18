package eu.tobiasheine.bitcoinwatcher.price_sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import eu.tobiasheine.bitcoinwatcher.api.BitcoinPriceApi;
import eu.tobiasheine.bitcoinwatcher.api.IBitcoinPriceApi;
import eu.tobiasheine.bitcoinwatcher.dao.storage.Storage;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.HandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.WearableNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private final BitcoinPriceHandler priceHandler;
    private final IBitcoinPriceApi bitcoinPriceApi;

    public SyncAdapter(final Context context, final boolean autoInitialize, final BitcoinPriceHandler priceHandler) {
        super(context, autoInitialize);

        this.priceHandler = priceHandler;
        this.bitcoinPriceApi = new BitcoinPriceApi();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        try {
            priceHandler.handleNewBitcoinPrice(bitcoinPriceApi.getCurrentPrice());
        } catch (Exception e) {
            Log.e("CurrentPriceSyncAdapter", Log.getStackTraceString(e));
        }
    }
}
