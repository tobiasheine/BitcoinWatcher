package eu.tobiasheine.bitcoinwatcher.ui;

public class WatchFaceViewModel {

    public final String hourString;
    public final String minuteString;
    public final String bitcoinPriceString;

    public WatchFaceViewModel(String hourString, String minuteString, String bitcoinPriceString) {
        this.hourString = hourString;
        this.minuteString = minuteString;
        this.bitcoinPriceString = bitcoinPriceString;
    }
}
