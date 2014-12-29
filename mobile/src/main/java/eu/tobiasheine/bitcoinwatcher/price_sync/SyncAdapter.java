package eu.tobiasheine.bitcoinwatcher.price_sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import eu.tobiasheine.bitcoinwatcher.api.BpiService;
import eu.tobiasheine.bitcoinwatcher.storage.Storage;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.Notifications;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;
import retrofit.RestAdapter;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private final BitcoinPriceHandler priceHandler;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.priceHandler = new BitcoinPriceHandler(new Storage(context), new Notifications(context), new Settings(context));
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.coindesk.com/v1")
                .build();

        final BpiService service = restAdapter.create(BpiService.class);

        try {
            priceHandler.handleNewBitcoinPrice(service.getCurrentPrice());
        } catch (Exception e) {
            Log.e("CurrentPriceSyncAdapter", Log.getStackTraceString(e));
        }
    }
}
