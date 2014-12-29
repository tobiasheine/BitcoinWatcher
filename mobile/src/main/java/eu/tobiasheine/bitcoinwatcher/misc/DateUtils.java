package eu.tobiasheine.bitcoinwatcher.misc;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import eu.tobiasheine.bitcoinwatcher.models.BitcoinPrice;

public class DateUtils {

    public static String convertCurrentPriceStringForWidget(final BitcoinPrice bitcoinPrice)  throws Exception{
        final DateFormat inputFormat = new SimpleDateFormat("MMM dd, yyy HH:mm:ss zz");
        inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        final Date date = inputFormat.parse(bitcoinPrice.getTime().getUpdated());
        final DateFormat outputFormat = new SimpleDateFormat("HH:mm / dd.MM yyy");
        outputFormat.setTimeZone(Calendar.getInstance().getTimeZone());
        return outputFormat.format(date);
    }
}
