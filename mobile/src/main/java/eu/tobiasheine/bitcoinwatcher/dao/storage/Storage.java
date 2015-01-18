package eu.tobiasheine.bitcoinwatcher.dao.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;

import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;

public class Storage implements IStorage{

    private static final String CURRENT_PRICE_KEY = "current_price_key";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    @Inject
    public Storage(final Context context) {
        this.sharedPreferences = context.getSharedPreferences("BitcoinWatcher", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    @Override
    public void storeNewPrice(final BitcoinPriceDTO newBitcoinPriceDTO) {
        String currentPriceAsString = gson.toJson(newBitcoinPriceDTO);
        sharedPreferences.edit().putString(CURRENT_PRICE_KEY, currentPriceAsString).apply();
    }

    @Override
    public BitcoinPriceDTO getStoredPrice() {
        final String currentPriceAsString = sharedPreferences.getString(CURRENT_PRICE_KEY, null);

        if (currentPriceAsString == null) {
            return null;
        }

        return gson.fromJson(currentPriceAsString, BitcoinPriceDTO.class);
    }
}
