package eu.tobiasheine.bitcoinwatcher.di;

import dagger.Module;
import dagger.Provides;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IWearableNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.WearableNotifications;

@Module
public class WearableNotificationsModule {

    //No singleton since it has a lifecycle
    @Provides
    public IWearableNotifications provideSettings(final WearableNotifications wearableNotifications) {
        return wearableNotifications;
    }
}
