package eu.tobiasheine.bitcoinwatcher;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import eu.tobiasheine.bitcoinwatcher.notifier.CurrentPriceNotifier;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;
import eu.tobiasheine.bitcoinwatcher.sync.CurrentPriceSynchronization;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CurrentPriceSynchronization synchronization;
    private Settings settings;
    private CurrentPriceNotifier notifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_bitcoin_watcher);

        synchronization = new CurrentPriceSynchronization(this);
        settings = new Settings(this);
        notifier = new CurrentPriceNotifier(this);

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

    public void setSynchronization(final CurrentPriceSynchronization synchronization) {
        this.synchronization = synchronization;
    }

    public void setSettings(final Settings settings) {
        this.settings = settings;
    }

    public void setNotifier(final CurrentPriceNotifier notifier) {
        this.notifier = notifier;
    }
}
