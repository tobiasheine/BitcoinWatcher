package eu.tobiasheine.bitcoinwatcher.model;

import com.google.gson.annotations.SerializedName;

public class CurrentPriceBpi {

    @SerializedName("USD")
    private BitcoinBpi usd;

    @SerializedName("GBP")
    private BitcoinBpi gbp;

    @SerializedName("EUR")
    private BitcoinBpi eur;

    public BitcoinBpi getUsd() {
        return usd;
    }

    public BitcoinBpi getGbp() {
        return gbp;
    }

    public BitcoinBpi getEur() {
        return eur;
    }
}
