package eu.tobiasheine.bitcoinwatcher.api;


import eu.tobiasheine.bitcoinwatcher.models.BitcoinPrice;
import retrofit.Callback;
import retrofit.http.GET;

public interface BpiService {

    @GET("/bpi/currentprice.json")
    void getCurrentPrice(Callback<BitcoinPrice> currentPriceCallback);

    @GET("/bpi/currentprice.json")
    BitcoinPrice getCurrentPrice();

}
