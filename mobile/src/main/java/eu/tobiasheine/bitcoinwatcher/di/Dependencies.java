package eu.tobiasheine.bitcoinwatcher.di;

import javax.inject.Singleton;

import dagger.Component;
import eu.tobiasheine.bitcoinwatcher.BitcoinWatcherApplication;
import eu.tobiasheine.bitcoinwatcher.SettingsActivity;
import eu.tobiasheine.bitcoinwatcher.dao.storage.IStorage;
import eu.tobiasheine.bitcoinwatcher.price_sync.ISynchronization;
import eu.tobiasheine.bitcoinwatcher.price_sync.SyncService;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IHandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IWearableNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;
import eu.tobiasheine.bitcoinwatcher.widget.BitcoinWatcherWidgetProvider;

@Singleton
@Component(modules = {
        ContextModule.class,
        SettingsModule.class,
        StorageModule.class,
        SynchronizationModule.class,
        HandheldNotificationsModule.class, WearableNotificationsModule.class, GoogleApiClientModule.class})
public interface Dependencies {

    void inject(BitcoinWatcherWidgetProvider widgetProvider);

    void inject(SettingsActivity settingsActivity);

    void inject(SyncService syncService);

    IStorage getStorage();

    ISettings getSettings();

    IHandheldNotifications getHandheldNotifications();

    ISynchronization getSynchronization();
}
