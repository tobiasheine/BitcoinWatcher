package eu.tobiasheine.bitcoinwatcher.price_sync;

public interface ISynchronization {

    void syncNow();

    void syncPeriodic(final int intervalInMinutes);
}
