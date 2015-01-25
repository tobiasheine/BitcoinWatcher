package eu.tobiasheine.bitcoinwatcher.di;

import eu.tobiasheine.bitcoinwatcher.SettingsActivity;
import eu.tobiasheine.bitcoinwatcher.dao.storage.IStorage;
import eu.tobiasheine.bitcoinwatcher.price_sync.ISynchronization;
import eu.tobiasheine.bitcoinwatcher.price_sync.SyncService;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IHandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;
import eu.tobiasheine.bitcoinwatcher.widget.BitcoinWatcherWidgetProvider;

public class UtsDependencies extends MockDependencies implements Dependencies {

    @Override
    public void inject(final BitcoinWatcherWidgetProvider widgetProvider) {
        replaceDependencies(widgetProvider);
    }

    @Override
    public void inject(final SettingsActivity settingsActivity) {
        replaceDependencies(settingsActivity);
    }

    @Override
    public void inject(final SyncService syncService) {
        replaceDependencies(syncService);
    }

    @Override
    public IStorage getStorage() {
        return getDependencyOrMock(IStorage.class);
    }

    @Override
    public ISettings getSettings() {
        return getDependencyOrMock(ISettings.class);
    }

    @Override
    public IHandheldNotifications getHandheldNotifications() {
        return getDependencyOrMock(IHandheldNotifications.class);
    }

    @Override
    public ISynchronization getSynchronization() {
        return getDependencyOrMock(ISynchronization.class);
    }

}
