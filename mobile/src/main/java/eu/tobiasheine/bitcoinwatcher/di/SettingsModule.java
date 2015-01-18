package eu.tobiasheine.bitcoinwatcher.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;

@Module
public class SettingsModule {

    @Provides
    @Singleton
    public ISettings provideSettings(final Settings settings) {
        return settings;
    }

}
