package eu.tobiasheine.bitcoinwatcher.price_sync;

import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;

public interface IBitcoinPriceHandler {
    void handleNewBitcoinPrice(BitcoinPriceDTO newBitcoinPriceDTO);
}
