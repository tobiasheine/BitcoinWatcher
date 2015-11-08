package eu.tobiasheine.bitcoinwatcher;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import eu.tobiasheine.bitcoinwatcher.di.Dependencies;
import eu.tobiasheine.bitcoinwatcher.price_sync.ISynchronization;
import eu.tobiasheine.bitcoinwatcher.price_sync.Synchronization;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.HandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IHandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IWearableNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.WearableNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    IWearableNotifications wearableNotifications;

    @Inject
    IHandheldNotifications handheldNotifications;

    @Inject
    ISynchronization synchronization;

    @Inject
    ISettings settings;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_xml, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_bitcoin_watcher);

        final Dependencies dependencies = BitcoinWatcherApplication.getDependencies();
        dependencies.inject(this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sync:
                synchronization.syncNow();
                return true;

            /*case R.id.disclaimer:
                // TODO:
                return true;*/
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        wearableNotifications.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);

        wearableNotifications.disconnect();
    }
}
