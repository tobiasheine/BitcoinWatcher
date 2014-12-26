package eu.tobiasheine.bitcoinwatcher.api;


import eu.tobiasheine.bitcoinwatcher.model.CurrentPrice;
import retrofit.Callback;
import retrofit.http.GET;

public interface BpiService {

    @GET("/bpi/currentprice.json")
    void getCurrentPrice(Callback<CurrentPrice> currentPriceCallback);

}
