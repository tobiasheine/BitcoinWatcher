package eu.tobiasheine.bitcoinwatcher.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import eu.tobiasheine.bitcoinwatcher.api.BpiService;
import eu.tobiasheine.bitcoinwatcher.notifier.CurrentPriceNotifier;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;
import eu.tobiasheine.bitcoinwatcher.storage.CurrentPriceStorage;
import eu.tobiasheine.bitcoinwatcher.updater.CurrentPriceUpdater;
import retrofit.RestAdapter;

public class CurrentPriceSyncAdapter extends AbstractThreadedSyncAdapter {

    private final CurrentPriceUpdater updater;

    public CurrentPriceSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.updater = new CurrentPriceUpdater(new CurrentPriceStorage(context), new CurrentPriceNotifier(context), new Settings(context));
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.coindesk.com/v1")
                .build();

        final BpiService service = restAdapter.create(BpiService.class);

        try {
            updater.updateCurrentPrice(service.getCurrentPrice());
        } catch (Exception e) {
            Log.e("CurrentPriceSyncAdapter", Log.getStackTraceString(e));
        }
    }
}
