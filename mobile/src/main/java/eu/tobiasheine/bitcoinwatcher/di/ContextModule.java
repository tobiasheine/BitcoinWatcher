package eu.tobiasheine.bitcoinwatcher.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return context;
    }
}
