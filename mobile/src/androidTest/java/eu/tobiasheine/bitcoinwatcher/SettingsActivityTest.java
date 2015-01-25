package eu.tobiasheine.bitcoinwatcher;

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import android.view.MenuItem;

import eu.tobiasheine.bitcoinwatcher.di.UtsDependencies;
import eu.tobiasheine.bitcoinwatcher.price_sync.ISynchronization;
import eu.tobiasheine.bitcoinwatcher.price_sync.Synchronization;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.HandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IHandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.IWearableNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.WearableNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SettingsActivityTest extends ActivityUnitTestCase<SettingsActivity> {

    private SettingsActivity settingsActivity;

    private Settings settings;
    private Synchronization synchronization;
    private HandheldNotifications handheldNotifications;
    private WearableNotifications wearableNotifications;

    public SettingsActivityTest() {
        super(SettingsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        settings = mock(Settings.class);
        synchronization = mock(Synchronization.class);
        handheldNotifications = mock(HandheldNotifications.class);
        wearableNotifications = mock(WearableNotifications.class);

        UtsDependencies utsDependencies = new UtsDependencies();

        utsDependencies.replace(ISettings.class, settings);
        utsDependencies.replace(ISynchronization.class, synchronization);
        utsDependencies.replace(IHandheldNotifications.class, handheldNotifications);
        utsDependencies.replace(IWearableNotifications.class, wearableNotifications);

        BitcoinWatcherApplication.replaceDependencies(utsDependencies);

        startActivity(new Intent(), Bundle.EMPTY, null);

        settingsActivity = getActivity();
    }

    public void testTriggerPeriodicSyncOnSettingChange() throws Exception {
        // given
        int syncInterval = 20;
        when(settings.getSyncIntervalInMinutes()).thenReturn(syncInterval);

        // when
        settingsActivity.onSharedPreferenceChanged(null, Settings.KEY_SYNC);

        // then
        verify(synchronization).syncPeriodic(syncInterval);
    }

    public void testWidgetUpdateOnCurrencyChange() throws Exception {
        // when
        settingsActivity.onSharedPreferenceChanged(null, Settings.KEY_CURRENCY);

        // then
        verify(handheldNotifications).notifyWidget();
    }

    public void testWearableUpdateOnCurrencyChange() throws Exception {
        // when
        settingsActivity.onSharedPreferenceChanged(null, Settings.KEY_CURRENCY);

        // then
        verify(wearableNotifications).notifyWearable();
    }

    public void testSyncOnMenuEntry() throws Exception {
        // given
        final MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(R.id.sync);

        // when
        settingsActivity.onOptionsItemSelected(menuItem);

        // then
        verify(synchronization).syncNow();
    }
}