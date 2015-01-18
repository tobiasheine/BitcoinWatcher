package eu.tobiasheine.bitcoinwatcher.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.HandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IHandheldNotifications;

@Module
public class HandheldNotificationsModule {

    @Provides
    @Singleton
    public IHandheldNotifications provideHandheldNotifications(final HandheldNotifications handheldNotifications) {
        return handheldNotifications;
    }
}
