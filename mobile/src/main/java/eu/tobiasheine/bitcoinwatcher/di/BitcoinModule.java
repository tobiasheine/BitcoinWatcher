package eu.tobiasheine.bitcoinwatcher.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.tobiasheine.bitcoinwatcher.price_sync.BitcoinPriceHandler;
import eu.tobiasheine.bitcoinwatcher.price_sync.IBitcoinPriceHandler;

@Module
public class BitcoinModule {

    @Provides
    @Singleton
    public IBitcoinPriceHandler provide(final BitcoinPriceHandler bitcoinPriceHandler) {
        return bitcoinPriceHandler;
    }
}
