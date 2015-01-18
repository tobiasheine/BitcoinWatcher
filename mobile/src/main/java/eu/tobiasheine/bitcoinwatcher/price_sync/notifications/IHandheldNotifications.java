package eu.tobiasheine.bitcoinwatcher.price_sync.notifications;

import eu.tobiasheine.bitcoinwatcher.api.dto.BitcoinPriceDTO;

public interface IHandheldNotifications {

    public void notifyAboutNewPrice(final BitcoinPriceDTO bitcoinPriceDTO);

    public void notifyWidget();
}
