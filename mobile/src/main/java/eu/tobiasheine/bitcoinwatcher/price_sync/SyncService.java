package eu.tobiasheine.bitcoinwatcher.price_sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import javax.inject.Inject;

import eu.tobiasheine.bitcoinwatcher.BitcoinWatcherApplication;
import eu.tobiasheine.bitcoinwatcher.dao.storage.IStorage;
import eu.tobiasheine.bitcoinwatcher.di.Dependencies;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IHandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IWearableNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;

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

        final Dependencies dependencies = BitcoinWatcherApplication.getDependencies();
        dependencies.inject(this);

        wearableNotifications.connect();

        final IStorage storage = dependencies.getStorage();
        final IHandheldNotifications handheldNotifications = dependencies.getHandheldNotifications();
        final ISettings settings = dependencies.getSettings();

        final BitcoinPriceHandler bitcoinPriceHandler = new BitcoinPriceHandler(storage, handheldNotifications, settings, wearableNotifications);

        synchronized (syncServiceLock) {

            if (syncAdapter == null) {
                syncAdapter = new SyncAdapter(getApplicationContext(), true, bitcoinPriceHandler);
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
