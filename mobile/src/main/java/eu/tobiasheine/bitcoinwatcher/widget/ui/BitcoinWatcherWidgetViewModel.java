package eu.tobiasheine.bitcoinwatcher.widget.ui;

public class BitcoinWatcherWidgetViewModel {

    private final String currentPrice;
    private final String updatedAt;

    public BitcoinWatcherWidgetViewModel(final String currentPrice, final String updatedAt) {
        this.currentPrice = currentPrice;
        this.updatedAt = updatedAt;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
