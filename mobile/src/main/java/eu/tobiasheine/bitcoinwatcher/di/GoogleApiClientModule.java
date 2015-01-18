package eu.tobiasheine.bitcoinwatcher.di;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GoogleApiClientModule {

    private final Context context;

    public GoogleApiClientModule(Context context) {
        this.context = context;
    }

    //No singleton since it has a lifecycle
    @Provides
    public GoogleApiClient provideApiClient() {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }
}
