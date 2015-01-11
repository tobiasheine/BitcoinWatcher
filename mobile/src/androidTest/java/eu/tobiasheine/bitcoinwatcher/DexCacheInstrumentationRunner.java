package eu.tobiasheine.bitcoinwatcher;

import android.os.Bundle;
import android.test.InstrumentationTestRunner;

public class DexCacheInstrumentationRunner extends InstrumentationTestRunner{

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);

        System.setProperty("dexmaker.dexcache", getTargetContext().getCacheDir().getPath());
    }
}
