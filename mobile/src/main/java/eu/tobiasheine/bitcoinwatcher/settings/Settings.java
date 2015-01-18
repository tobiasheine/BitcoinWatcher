package eu.tobiasheine.bitcoinwatcher.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

import eu.tobiasheine.bitcoinwatcher.core.domain.Currency;

public class Settings implements ISettings{

    public static final String KEY_CURRENCY = "bitcoin_currency";
    public static final String KEY_SYNC = "sync_frequency";
    public static final String KEY_PRICE_LIMIT = "current_price_limit";

    private final SharedPreferences defaultSharedPrefs;

    @Inject
    public Settings(final Context context) {
        defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public Currency getSelectedCurrency(){
        return Currency.valueOf(defaultSharedPrefs.getString(KEY_CURRENCY, Currency.EUR.name()));
    }

    @Override
    public int getPriceChangeLimitInPercentage() {
        return (int) (defaultSharedPrefs.getFloat(KEY_PRICE_LIMIT, 0.1f) * 100);
    }

    @Override
    public int getSyncIntervalInMinutes(){
        return Integer.valueOf(defaultSharedPrefs.getString(KEY_SYNC,"15"));
    }
}
