package eu.tobiasheine.bitcoinwatcher.di;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.HandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IHandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IWearableNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.WearableNotifications;

@Module
public class NotificationsModule {

    @Provides
    @Singleton
    public IHandheldNotifications provideHandheldNotifications(final HandheldNotifications handheldNotifications) {
        return handheldNotifications;
    }

    //No singleton since it has a lifecycle
    @Provides
    public IWearableNotifications provideWearableNotifications(final WearableNotifications wearableNotifications) {
        return wearableNotifications;
    }

    //No singleton since it has a lifecycle
    @Provides
    public GoogleApiClient provideApiClient(final Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }
}
