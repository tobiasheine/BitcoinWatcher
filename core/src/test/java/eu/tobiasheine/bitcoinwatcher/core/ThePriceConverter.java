package eu.tobiasheine.bitcoinwatcher.core;

import org.junit.Test;

import eu.tobiasheine.bitcoinwatcher.core.domain.BitcoinPrice;
import eu.tobiasheine.bitcoinwatcher.core.domain.Currency;

import static org.junit.Assert.assertEquals;

public class ThePriceConverter {

    final PriceConverter priceConverter = new PriceConverter();

    @Test
    public void convertToBytes() throws Exception {
        // given
        final BitcoinPrice price = new BitcoinPrice(Currency.EUR, 100f, "updatedAt");

        // when
        byte[] bytes = priceConverter.convertToByte(price);
        BitcoinPrice restoredPrice = priceConverter.convertToPrice(bytes);

        // then
        assertEquals(price, restoredPrice);


    }
}