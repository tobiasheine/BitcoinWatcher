package eu.tobiasheine.bitcoinwatcher.misc;

import android.test.AndroidTestCase;

import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;
import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceUpdatedAtDTO;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TheDateUtils extends AndroidTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());
    }

    public void testConvertsDateForWidget() throws Exception {
        // given
        final String dateString = "Dec 31, 2014 11:04:00 UTC";

        final BitcoinPriceDTO price = mock(BitcoinPriceDTO.class);
        final BitcoinPriceUpdatedAtDTO time = mock(BitcoinPriceUpdatedAtDTO.class);
        when(time.getUpdated()).thenReturn(dateString);
        when(price.getTime()).thenReturn(time);

        // when
        String convertedString = DateUtils.convertCurrentPriceStringForWidget(price);

        // then
        assertEquals("12:04 / 31.12.2014", convertedString);
    }
}