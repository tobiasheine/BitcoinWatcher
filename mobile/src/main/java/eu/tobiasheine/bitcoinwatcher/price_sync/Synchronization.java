package eu.tobiasheine.bitcoinwatcher.price_sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

public class Synchronization {

    final Account dummyAccount;

    public Synchronization(Context context) {

        this.dummyAccount = new Account("dummyaccount", "eu.tobiasheine.bitcoinwatcher");

        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        accountManager.addAccountExplicitly(dummyAccount, null, null);

        ContentResolver.setSyncAutomatically(
                dummyAccount, ContentProviderStub.AUTHORITY, true);
    }

    public void syncNow() {
        ContentResolver.requestSync(dummyAccount, ContentProviderStub.AUTHORITY, Bundle.EMPTY);
    }

    public void syncPeriodic(final int intervalInSeconds) {
        ContentResolver.addPeriodicSync(dummyAccount, ContentProviderStub.AUTHORITY, Bundle.EMPTY, intervalInSeconds);
    }
}
