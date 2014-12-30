package eu.tobiasheine.bitcoinwatcher.api;

import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;
import retrofit.RestAdapter;

public class BitcoinPriceApi implements IBitcoinPriceApi {

    private final IBitcoinPriceApi service;

    public BitcoinPriceApi() {
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.coindesk.com/v1")
                .build();

        service = restAdapter.create(IBitcoinPriceApi.class);
    }

    @Override
    public BitcoinPriceDTO getCurrentPrice() {
        return service.getCurrentPrice();
    }
}
