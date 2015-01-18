package eu.tobiasheine.bitcoinwatcher;

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import android.view.MenuItem;

import eu.tobiasheine.bitcoinwatcher.price_sync.Synchronization;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.HandheldNotifications;
import eu.tobiasheine.bitcoinwatcher.price_sync.notifications.WearableNotifications;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;
import eu.tobiasheine.bitcoinwatcher.widget.ui.BitcoinWatcherWidgetViewModel;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
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




        startActivity(new Intent(), Bundle.EMPTY, null);

        settings = mock(Settings.class);
        synchronization = mock(Synchronization.class);
        handheldNotifications = mock(HandheldNotifications.class);
        wearableNotifications = mock(WearableNotifications.class);

        settingsActivity = getActivity();

        settingsActivity.setSettings(settings);
        settingsActivity.setSynchronization(synchronization);
        settingsActivity.setHandheldNotifications(handheldNotifications);
        settingsActivity.setWearableNotifications(wearableNotifications);

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