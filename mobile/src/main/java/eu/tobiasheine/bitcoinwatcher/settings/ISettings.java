package eu.tobiasheine.bitcoinwatcher.settings;

import eu.tobiasheine.bitcoinwatcher.core.domain.Currency;

public interface ISettings {

    Currency getSelectedCurrency();

    int getPriceChangeLimitInPercentage();

    int getSyncIntervalInMinutes();
}
