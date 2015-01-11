package eu.tobiasheine.bitcoinwatcher.price_sync;

import android.content.Context;
import android.content.SharedPreferences;

import eu.tobiasheine.bitcoinwatcher.core.domain.BitcoinPrice;
import eu.tobiasheine.bitcoinwatcher.core.domain.Currency;

public class Storage {

    private static final String KEY_CURRENCY = "CURRENCY";
    private static final String KEY_PRICE = "PRICE";
    private final SharedPreferences sharedPreferences;

    public Storage(Context context) {
        sharedPreferences = context.getSharedPreferences("Bitcoin Watcher Watchface", Context.MODE_PRIVATE);
    }

    public void storePrice(BitcoinPrice bitcoinPrice) {
        sharedPreferences.edit().putString(KEY_CURRENCY, bitcoinPrice.currency.name()).apply();
        sharedPreferences.edit().putFloat(KEY_PRICE, bitcoinPrice.rate).apply();
    }

    public Currency getCurrency() {
        return Currency.valueOf(sharedPreferences.getString(KEY_CURRENCY, "EUR"));
    }

    public float getPrice() {
        return sharedPreferences.getFloat(KEY_PRICE, -1f);
    }
}
