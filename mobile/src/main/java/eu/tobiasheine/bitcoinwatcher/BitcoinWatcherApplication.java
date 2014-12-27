package eu.tobiasheine.bitcoinwatcher;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.os.Bundle;

import eu.tobiasheine.bitcoinwatcher.sync.DummyProvider;

public class BitcoinWatcherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Create the account type and default account
        Account newAccount = new Account("dummyaccount", "eu.tobiasheine.bitcoinwatcher");
        AccountManager accountManager = (AccountManager) this.getSystemService(ACCOUNT_SERVICE);
        // If the account already exists no harm is done but
        // a warning will be logged.
        accountManager.addAccountExplicitly(newAccount, null, null);

        ContentResolver.setSyncAutomatically(
                newAccount, "eu.tobiasheine.bitcoinwatcher.content", true);

        ContentResolver.addPeriodicSync(newAccount,DummyProvider.AUTHORITY,Bundle.EMPTY,60);
    }
}
