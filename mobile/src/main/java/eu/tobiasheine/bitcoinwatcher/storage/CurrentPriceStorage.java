package eu.tobiasheine.bitcoinwatcher.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import eu.tobiasheine.bitcoinwatcher.model.CurrentPrice;

public class CurrentPriceStorage {

    private static final String CURRENT_PRICE_KEY = "current_price_key";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public CurrentPriceStorage(final Context context) {
        this.sharedPreferences = context.getSharedPreferences("BitcoinWatcher", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void storeNewCurrentPrice(final CurrentPrice newCurrentPrice) {
        String currentPriceAsString = gson.toJson(newCurrentPrice);
        sharedPreferences.edit().putString(CURRENT_PRICE_KEY, currentPriceAsString).apply();
    }

    public CurrentPrice getLatestCurrentPrice() {
        final String currentPriceAsString = sharedPreferences.getString(CURRENT_PRICE_KEY, null);

        if (currentPriceAsString == null) {
            return null;
        }

        return gson.fromJson(currentPriceAsString, CurrentPrice.class);
    }
}
