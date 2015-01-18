package eu.tobiasheine.bitcoinwatcher.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.tobiasheine.bitcoinwatcher.dao.storage.IStorage;
import eu.tobiasheine.bitcoinwatcher.dao.storage.Storage;

@Module
public class StorageModule {

    @Provides
    @Singleton
    public IStorage provideStorage(final Storage storage) {
        return storage;
    }
}
