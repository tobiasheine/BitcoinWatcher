package eu.tobiasheine.bitcoinwatcher.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CurrentPriceSyncService extends Service{

    private static final Object syncAdapterLock = new Object();

    private CurrentPriceSyncAdapter syncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (syncAdapterLock) {
            if (syncAdapter == null)
                syncAdapter = new CurrentPriceSyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
