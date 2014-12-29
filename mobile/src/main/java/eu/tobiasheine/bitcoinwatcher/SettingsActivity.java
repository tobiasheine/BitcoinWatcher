package eu.tobiasheine.bitcoinwatcher;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import eu.tobiasheine.bitcoinwatcher.price_sync.Synchronization;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.Notifications;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Synchronization synchronization;
    private Settings settings;
    private Notifications notifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_bitcoin_watcher);

        synchronization = new Synchronization(this);
        settings = new Settings(this);
        notifier = new Notifications(this);

        synchronization.syncNow();
        synchronization.syncPeriodic(settings.getSyncIntervalInMinutes());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(Settings.KEY_CURRENCY)) {
            notifier.notifyWidget();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void setSynchronization(final Synchronization synchronization) {
        this.synchronization = synchronization;
    }

    public void setSettings(final Settings settings) {
        this.settings = settings;
    }

    public void setNotifier(final Notifications notifier) {
        this.notifier = notifier;
    }
}
