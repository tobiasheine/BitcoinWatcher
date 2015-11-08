package eu.tobiasheine.bitcoinwatcher.widget.ui;

import android.content.Context;
import android.util.Log;

import java.util.Locale;

import eu.tobiasheine.bitcoinwatcher.R;
import eu.tobiasheine.bitcoinwatcher.misc.DateUtils;
import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;
import eu.tobiasheine.bitcoinwatcher.core.domain.Currency;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;

public class BitcoinWatcherViewModelFactory {

    public static BitcoinWatcherWidgetViewModel create(final Context context, final ISettings settings, final BitcoinPriceDTO bitcoinPriceDTO) {

        if (bitcoinPriceDTO == null) {
            return createEmptyViewModel(context);
        }

        String updatedAt = "";
        try {
            updatedAt = DateUtils.convertCurrentPriceStringForWidget(bitcoinPriceDTO);

        } catch (Exception e) {
            Log.e("ViewModelFactory", Log.getStackTraceString(e));
        }

        final Currency selectedCurrency = settings.getSelectedCurrency();

        String currentPriceAsString = "";

        switch (selectedCurrency) {
            case EUR:
                currentPriceAsString = String.format("%.2f", bitcoinPriceDTO.getBpi().getEur().rate_float);
                break;

            case GBP:
                currentPriceAsString = String.format("%.2f", bitcoinPriceDTO.getBpi().getGbp().rate_float);
                break;

            case USD:
                currentPriceAsString = String.format("%.2f", bitcoinPriceDTO.getBpi().getUsd().rate_float);
                break;
        }

        currentPriceAsString = currentPriceAsString + " " + selectedCurrency.name();

        return new BitcoinWatcherWidgetViewModel(currentPriceAsString, updatedAt);
    }

    private static BitcoinWatcherWidgetViewModel createEmptyViewModel(final Context context) {
        return new BitcoinWatcherWidgetViewModel(context.getString(R.string.bitcoin_watcher_widget_empty_price), "");
    }
}
