package eu.tobiasheine.bitcoinwatcher.model;

public class CurrentPrice {

    private CurrentPriceTime time;
    private String disclaimer;
    private CurrentPriceBpi bpi;

    public CurrentPriceTime getTime() {
        return time;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public CurrentPriceBpi getBpi() {
        return bpi;
    }
}
