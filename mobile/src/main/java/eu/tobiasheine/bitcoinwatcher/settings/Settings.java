package eu.tobiasheine.bitcoinwatcher.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

    public static final String KEY_CURRENCY = "bitcoin_currency";
    public static final String KEY_SYNC = "sync_frequency";
    public static final String KEY_PRICE_LIMIT = "current_price_limit";

    private final SharedPreferences defaultSharedPrefs;

    public Settings(final Context context) {
        defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Currency getSelectedCurrency(){
        return Currency.valueOf(defaultSharedPrefs.getString(KEY_CURRENCY, Currency.EUR.name()));
    }

    public int getPriceChangeLimitInPercentage() {
        return (int) (defaultSharedPrefs.getFloat(KEY_PRICE_LIMIT, 0.1f) * 100);
    }

    public int getSyncIntervalInMinutes(){
        return Integer.valueOf(defaultSharedPrefs.getString(KEY_SYNC,"15"));
    }
}
