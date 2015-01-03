package eu.tobiasheine.bitcoinwatcher;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import eu.tobiasheine.bitcoinwatcher.dao.storage.Storage;
import eu.tobiasheine.bitcoinwatcher.price_sync.Synchronization;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.HandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.WearableNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Synchronization synchronization;
    private Settings settings;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sync:
                synchronization.syncNow();
                return true;

            case R.id.disclaimer:
                // TODO:
                return true;
        }

        return false;
    }

    private HandheldNotifications handheldNotifications;

    private WearableNotifications wearableNotifications;
    private GoogleApiClient googleApiClient;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_xml, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_bitcoin_watcher);

        synchronization = new Synchronization(this);
        settings = new Settings(this);
        handheldNotifications = new HandheldNotifications(this);

        synchronization.syncNow();
        synchronization.syncPeriodic(settings.getSyncIntervalInMinutes());

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();


        final Storage storage = new Storage(this);

        wearableNotifications = new WearableNotifications(googleApiClient, storage, settings);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(Settings.KEY_CURRENCY)) {
            handheldNotifications.notifyWidget();
            wearableNotifications.notifyWearable();
        }

        if (key.equals(Settings.KEY_SYNC)) {
            synchronization.syncPeriodic(settings.getSyncIntervalInMinutes());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);

        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

    }

    public void setSynchronization(final Synchronization synchronization) {
        this.synchronization = synchronization;
    }

    public void setSettings(final Settings settings) {
        this.settings = settings;
    }

    public void setHandheldNotifications(final HandheldNotifications handheldNotifications) {
        this.handheldNotifications = handheldNotifications;
    }

    public void setWearableNotifications(final WearableNotifications wearableNotifications) {
        this.wearableNotifications = wearableNotifications;
    }
}
