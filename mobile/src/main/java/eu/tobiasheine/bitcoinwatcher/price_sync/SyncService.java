package eu.tobiasheine.bitcoinwatcher.price_sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import javax.inject.Inject;

import eu.tobiasheine.bitcoinwatcher.BitcoinWatcherApplication;
import eu.tobiasheine.bitcoinwatcher.di.Dependencies;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IWearableNotifications;

public class SyncService extends Service{

    private static final Object syncServiceLock = new Object();

    @Inject
    ISynchronization synchronization;

    @Inject
    IWearableNotifications wearableNotifications;

    private SyncAdapter syncAdapter;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronization.syncNow();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BitcoinWatcherApplication.getDependencies().inject(this);

        wearableNotifications.connect();

        synchronized (syncServiceLock) {

            if (syncAdapter == null) {
                syncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        wearableNotifications.disconnect();
    }
}
