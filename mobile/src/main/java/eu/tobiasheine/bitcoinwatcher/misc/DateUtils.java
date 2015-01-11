package eu.tobiasheine.bitcoinwatcher.misc;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;

public class DateUtils {

    public static String convertCurrentPriceStringForWidget(final BitcoinPriceDTO bitcoinPriceDTO)  throws Exception{
        final DateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss zz", Locale.ENGLISH);
        inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        final Date date = inputFormat.parse(bitcoinPriceDTO.getTime().getUpdated());
        final DateFormat outputFormat = new SimpleDateFormat("HH:mm / dd.MM.yyy");
        outputFormat.setTimeZone(Calendar.getInstance().getTimeZone());
        return outputFormat.format(date);
    }
}
