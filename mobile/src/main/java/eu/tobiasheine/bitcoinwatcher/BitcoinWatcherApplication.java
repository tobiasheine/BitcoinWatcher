package eu.tobiasheine.bitcoinwatcher;

import android.app.Application;

import eu.tobiasheine.bitcoinwatcher.di.ContextModule;
import eu.tobiasheine.bitcoinwatcher.di.DaggerDependencies;

import eu.tobiasheine.bitcoinwatcher.di.Dependencies;
import eu.tobiasheine.bitcoinwatcher.di.NotificationsModule;
import eu.tobiasheine.bitcoinwatcher.di.SettingsModule;
import eu.tobiasheine.bitcoinwatcher.di.SynchronizationModule;
import eu.tobiasheine.bitcoinwatcher.price_sync.ISynchronization;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;

public class BitcoinWatcherApplication extends Application {

    private static Dependencies dependencies;

    @Override
    public void onCreate() {
        super.onCreate();

        dependencies = DaggerDependencies.builder().
                contextModule(new ContextModule(this)).
                settingsModule(new SettingsModule()).
                synchronizationModule(new SynchronizationModule()).
                notificationsModule(new NotificationsModule()).
                build();


        final ISynchronization synchronization = dependencies.getSynchronization();
        final ISettings settings = dependencies.getSettings();

        synchronization.syncNow();
        synchronization.syncPeriodic(settings.getSyncIntervalInMinutes());
    }

    public static Dependencies getDependencies() {
        return dependencies;
    }

    public static void replaceDependencies(final Dependencies dependencies) {
        BitcoinWatcherApplication.dependencies = dependencies;
    }
}
