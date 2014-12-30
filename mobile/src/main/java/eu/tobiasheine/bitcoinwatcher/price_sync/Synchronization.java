package eu.tobiasheine.bitcoinwatcher.price_sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

public class Synchronization {

    final Account syncAccount;

    public Synchronization(Context context) {

        this.syncAccount = new Account("Sync Account", "eu.tobiasheine.bitcoinwatcher");

        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        accountManager.addAccountExplicitly(syncAccount, null, null);

        ContentResolver.setSyncAutomatically(
                syncAccount, ContentProviderStub.AUTHORITY, true);
    }

    public void syncNow() {
        ContentResolver.requestSync(syncAccount, ContentProviderStub.AUTHORITY, Bundle.EMPTY);
    }

    public void syncPeriodic(final int intervalInSeconds) {
        ContentResolver.addPeriodicSync(syncAccount, ContentProviderStub.AUTHORITY, Bundle.EMPTY, intervalInSeconds);
    }
}
