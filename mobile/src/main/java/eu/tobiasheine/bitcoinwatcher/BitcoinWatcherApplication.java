package eu.tobiasheine.bitcoinwatcher;

import android.app.Application;

import eu.tobiasheine.bitcoinwatcher.di.ContextModule;
import eu.tobiasheine.bitcoinwatcher.di.Dagger_Dependencies;
import eu.tobiasheine.bitcoinwatcher.di.Dependencies;
import eu.tobiasheine.bitcoinwatcher.di.GoogleApiClientModule;
import eu.tobiasheine.bitcoinwatcher.di.HandheldNotificationsModule;
import eu.tobiasheine.bitcoinwatcher.di.SettingsModule;
import eu.tobiasheine.bitcoinwatcher.di.SynchronizationModule;
import eu.tobiasheine.bitcoinwatcher.di.WearableNotificationsModule;
import eu.tobiasheine.bitcoinwatcher.price_sync.ISynchronization;
import eu.tobiasheine.bitcoinwatcher.price_sync.Synchronization;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;

public class BitcoinWatcherApplication extends Application {

    private static BitcoinWatcherApplication INSTANCE;

    private Dependencies dependencies;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        dependencies = Dagger_Dependencies.builder().
                contextModule(new ContextModule(this)).
                settingsModule(new SettingsModule()).
                synchronizationModule(new SynchronizationModule()).
                handheldNotificationsModule(new HandheldNotificationsModule()).
                googleApiClientModule(new GoogleApiClientModule(this)).
                wearableNotificationsModule(new WearableNotificationsModule()).
                build();


        final ISynchronization synchronization = dependencies.getSynchronization();
        final ISettings settings = dependencies.getSettings();

        synchronization.syncNow();
        synchronization.syncPeriodic(settings.getSyncIntervalInMinutes());
    }

    public static Dependencies getDependencies() {
        return INSTANCE.dependencies;
    }

    public static void replaceDependencies(final Dependencies dependencies) {
        INSTANCE.dependencies = dependencies;
    }
}
