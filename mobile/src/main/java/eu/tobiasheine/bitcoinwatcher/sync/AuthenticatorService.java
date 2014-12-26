package eu.tobiasheine.bitcoinwatcher.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {

    private AuthenticatorStub authenticator;
    @Override
    public void onCreate() {
        authenticator = new AuthenticatorStub(this);
    }

    /*
    * When the system binds to this Service to make the RPC call
    * return the authenticator’s IBinder.
    */
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
