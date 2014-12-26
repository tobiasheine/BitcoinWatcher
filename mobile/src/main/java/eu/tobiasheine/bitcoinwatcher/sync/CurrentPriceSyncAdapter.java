package eu.tobiasheine.bitcoinwatcher.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import eu.tobiasheine.bitcoinwatcher.api.BpiService;
import eu.tobiasheine.bitcoinwatcher.model.CurrentPrice;
import retrofit.RestAdapter;

public class CurrentPriceSyncAdapter extends AbstractThreadedSyncAdapter {

    public CurrentPriceSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.coindesk.com/v1")
                .build();

        BpiService service = restAdapter.create(BpiService.class);

        CurrentPrice currentPrice = service.getCurrentPrice();

        Log.d("Bitcoin Watcher", currentPrice.getDisclaimer());

        String newPrice = new Gson().toJson(currentPrice);
    }
}
