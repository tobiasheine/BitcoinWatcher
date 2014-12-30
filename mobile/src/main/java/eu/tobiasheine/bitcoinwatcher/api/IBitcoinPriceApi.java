package eu.tobiasheine.bitcoinwatcher.api;


import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;
import retrofit.Callback;
import retrofit.http.GET;

public interface IBitcoinPriceApi {

    @GET("/bpi/currentprice.json")
    BitcoinPriceDTO getCurrentPrice();

}
