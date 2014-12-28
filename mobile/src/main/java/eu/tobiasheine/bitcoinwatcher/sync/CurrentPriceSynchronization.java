package eu.tobiasheine.bitcoinwatcher.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

public class CurrentPriceSynchronization {

    final Account dummyAccount;

    public CurrentPriceSynchronization(Context context) {

        this.dummyAccount = new Account("dummyaccount", "eu.tobiasheine.bitcoinwatcher");

        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        accountManager.addAccountExplicitly(dummyAccount, null, null);

        ContentResolver.setSyncAutomatically(
                dummyAccount, DummyProvider.AUTHORITY, true);
    }

    public void syncNow() {
        ContentResolver.requestSync(dummyAccount, DummyProvider.AUTHORITY, Bundle.EMPTY);
    }

    public void syncPeriodic(final int intervalInSeconds) {
        ContentResolver.addPeriodicSync(dummyAccount, DummyProvider.AUTHORITY, Bundle.EMPTY, intervalInSeconds);
    }
}
