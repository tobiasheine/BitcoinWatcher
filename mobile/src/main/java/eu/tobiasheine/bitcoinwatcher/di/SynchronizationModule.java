package eu.tobiasheine.bitcoinwatcher.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.tobiasheine.bitcoinwatcher.price_sync.ISynchronization;
import eu.tobiasheine.bitcoinwatcher.price_sync.Synchronization;

@Module
public class SynchronizationModule {

    @Provides
    @Singleton
    public ISynchronization provideSynchronization(final Synchronization synchronization){
        return synchronization;
    }
}
