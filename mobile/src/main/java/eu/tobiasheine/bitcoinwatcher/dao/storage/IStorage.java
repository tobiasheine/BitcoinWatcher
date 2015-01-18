package eu.tobiasheine.bitcoinwatcher.dao.storage;

import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;

public interface IStorage {

    void storeNewPrice(final BitcoinPriceDTO newBitcoinPriceDTO);

    BitcoinPriceDTO getStoredPrice();
}
