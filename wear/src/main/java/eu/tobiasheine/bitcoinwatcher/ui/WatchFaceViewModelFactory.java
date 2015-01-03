package eu.tobiasheine.bitcoinwatcher.ui;

import android.content.Context;
import android.text.format.Time;

import eu.tobiasheine.bitcoinwatcher.R;
import eu.tobiasheine.bitcoinwatcher.Storage;

public class WatchFaceViewModelFactory {

    private final Storage storage;
    private final Context context;

    public WatchFaceViewModelFactory(Storage storage, Context context) {
        this.storage = storage;
        this.context = context;
    }

    public WatchFaceViewModel createViewModel(final Time time) {

        final String hourString = String.valueOf(time.hour);
        final String minuteString = formatTwoDigitNumber(time.minute);

        float price = storage.getPrice();
        final String bitcoinPrice = String.format("%.2f", price);
        final String bitcoinCurrency = storage.getCurrency().name();


        final String bitcoinPriceString;
        if (price < 0 ) {
            bitcoinPriceString = context.getString(R.string.watch_face_empty_price_model);
        }else {
            bitcoinPriceString = bitcoinPrice+" "+bitcoinCurrency;
        }

        return new WatchFaceViewModel(hourString, minuteString, bitcoinPriceString);
    }

    private String formatTwoDigitNumber(int hour) {
        return String.format("%02d", hour);
    }
}
