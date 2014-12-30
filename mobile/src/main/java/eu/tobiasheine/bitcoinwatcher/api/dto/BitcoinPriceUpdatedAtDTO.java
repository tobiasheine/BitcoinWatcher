package eu.tobiasheine.bitcoinwatcher.api.dto;

public class BitcoinPriceUpdatedAtDTO {

    private String updated;
    private String updatedISO;
    private String updateduk;


    public String getUpdated() {
        return updated;
    }

    public String getUpdatedISO() {
        return updatedISO;
    }

    public String getUpdateduk() {
        return updateduk;
    }
}
