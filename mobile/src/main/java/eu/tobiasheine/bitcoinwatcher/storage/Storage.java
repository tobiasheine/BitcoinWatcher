package eu.tobiasheine.bitcoinwatcher.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import eu.tobiasheine.bitcoinwatcher.models.BitcoinPrice;

public class Storage {

    private static final String CURRENT_PRICE_KEY = "current_price_key";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public Storage(final Context context) {
        this.sharedPreferences = context.getSharedPreferences("BitcoinWatcher", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void storeNewCurrentPrice(final BitcoinPrice newBitcoinPrice) {
        String currentPriceAsString = gson.toJson(newBitcoinPrice);
        sharedPreferences.edit().putString(CURRENT_PRICE_KEY, currentPriceAsString).apply();
    }

    public BitcoinPrice getLatestCurrentPrice() {
        final String currentPriceAsString = sharedPreferences.getString(CURRENT_PRICE_KEY, null);

        if (currentPriceAsString == null) {
            return null;
        }

        return gson.fromJson(currentPriceAsString, BitcoinPrice.class);
    }
}
