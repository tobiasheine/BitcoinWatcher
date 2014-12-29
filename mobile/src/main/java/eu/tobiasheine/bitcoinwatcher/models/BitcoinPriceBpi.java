package eu.tobiasheine.bitcoinwatcher.models;

import com.google.gson.annotations.SerializedName;

public class BitcoinPriceBpi {

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
