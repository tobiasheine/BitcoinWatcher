package eu.tobiasheine.bitcoinwatcher.core.domain;

import java.io.Serializable;

public class BitcoinPrice implements Serializable{

    public final Currency currency;
    public final float rate;
    public final String updatedAt;

    public BitcoinPrice(final Currency currency, final float rate, final String updatedAt) {
        this.currency = currency;
        this.rate = rate;
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitcoinPrice that = (BitcoinPrice) o;

        if (Float.compare(that.rate, rate) != 0) return false;
        if (currency != that.currency) return false;
        if (!updatedAt.equals(that.updatedAt)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = currency.hashCode();
        result = 31 * result + (rate != +0.0f ? Float.floatToIntBits(rate) : 0);
        result = 31 * result + updatedAt.hashCode();
        return result;
    }
}
