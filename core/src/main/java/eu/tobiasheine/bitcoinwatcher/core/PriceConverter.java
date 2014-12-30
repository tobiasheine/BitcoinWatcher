package eu.tobiasheine.bitcoinwatcher.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import eu.tobiasheine.bitcoinwatcher.core.domain.BitcoinPrice;

public class PriceConverter {

    public byte[] convertToByte(final BitcoinPrice bitcoinPrice) throws IOException{
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(bitcoinPrice);
        return out.toByteArray();
    }

    public BitcoinPrice convertToPrice(final byte[] data) throws IOException, ClassNotFoundException {
        final ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return (BitcoinPrice) is.readObject();
    }
}
