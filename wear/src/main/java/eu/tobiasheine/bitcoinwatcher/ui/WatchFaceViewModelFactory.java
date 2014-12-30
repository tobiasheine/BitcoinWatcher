package eu.tobiasheine.bitcoinwatcher.ui;

import android.text.format.Time;

import eu.tobiasheine.bitcoinwatcher.Storage;

public class WatchFaceViewModelFactory {

    private final Storage storage;

    public WatchFaceViewModelFactory(Storage storage) {
        this.storage = storage;
    }

    public WatchFaceViewModel createViewModel(final Time time) {

        final String hourString = String.valueOf(time.hour);
        final String minuteString = formatTwoDigitNumber(time.minute);

        float price = storage.getPrice();
        final String bitcoinPrice = String.format("%.2f", price);
        final String bitcoinCurrency = storage.getCurrency().name();


        final String bitcoinPriceString;
        if (price < 0 ) {
            bitcoinPriceString = "not yet synced";
        }else {
            bitcoinPriceString = bitcoinPrice+" "+bitcoinCurrency;
        }

        return new WatchFaceViewModel(hourString, minuteString, bitcoinPriceString);
    }

    private String formatTwoDigitNumber(int hour) {
        return String.format("%02d", hour);
    }
}
