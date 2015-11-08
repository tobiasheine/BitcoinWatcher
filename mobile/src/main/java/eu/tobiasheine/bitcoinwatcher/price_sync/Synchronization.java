package eu.tobiasheine.bitcoinwatcher.price_sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import javax.inject.Inject;

public class Synchronization implements ISynchronization{

    final Account syncAccount;

    @Inject
    public Synchronization(Context context) {

        this.syncAccount = new Account("Sync Account", "eu.tobiasheine.bitcoinwatcher");

        final AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        accountManager.addAccountExplicitly(syncAccount, null, null);

        ContentResolver.setSyncAutomatically(
                syncAccount, ContentProviderStub.AUTHORITY, true);
    }

    @Override
    public void syncNow() {
        ContentResolver.requestSync(syncAccount, ContentProviderStub.AUTHORITY, Bundle.EMPTY);
    }

    @Override
    public void syncPeriodic(final int intervalInMinutes) {
        ContentResolver.addPeriodicSync(syncAccount, ContentProviderStub.AUTHORITY, Bundle.EMPTY, intervalInMinutes * 60);
    }
}
