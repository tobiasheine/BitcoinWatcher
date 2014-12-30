package eu.tobiasheine.bitcoinwatcher.api.dto;

import com.google.gson.annotations.SerializedName;

public class BitcoinPriceBpiDTO {

    @SerializedName("USD")
    private BitcoinBpiDTO usd;

    @SerializedName("GBP")
    private BitcoinBpiDTO gbp;

    @SerializedName("EUR")
    private BitcoinBpiDTO eur;

    public BitcoinBpiDTO getUsd() {
        return usd;
    }

    public BitcoinBpiDTO getGbp() {
        return gbp;
    }

    public BitcoinBpiDTO getEur() {
        return eur;
    }
}
