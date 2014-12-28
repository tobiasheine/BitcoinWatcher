package eu.tobiasheine.bitcoinwatcher;

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;

import eu.tobiasheine.bitcoinwatcher.settings.Settings;
import eu.tobiasheine.bitcoinwatcher.sync.CurrentPriceSynchronization;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SettingsActivityTest extends ActivityUnitTestCase<SettingsActivity> {

    private SettingsActivity settingsActivity;
    private Settings settings;
    private CurrentPriceSynchronization synchronization;

    public SettingsActivityTest() {
        super(SettingsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        startActivity(new Intent(), Bundle.EMPTY, null);

        // http://stackoverflow.com/questions/12267572/mockito-dexmaker-on-android
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        settings = mock(Settings.class);
        synchronization = mock(CurrentPriceSynchronization.class);

        settingsActivity = getActivity();

        settingsActivity.setSettings(settings);
        settingsActivity.setSynchronization(synchronization);

    }

    //TODO: apply test after DI
    /*
    public void testFirstTriggerSyncThenSetupPeriodicSync() throws Exception {
        //given
        int syncInterval = 20;
        when(settings.getSyncIntervalInMinutes()).thenReturn(syncInterval);

        // when
        settingsActivity.onCreate(Bundle.EMPTY);

        // then
        InOrder order = inOrder(synchronization);
        order.verify(synchronization).syncNow();
        order.verify(synchronization).syncPeriodic(syncInterval);
    }*/

    public void testTriggerPeriodicSyncOnSettingChange() throws Exception {
        // given
        int syncInterval = 20;
        when(settings.getSyncIntervalInMinutes()).thenReturn(syncInterval);

        // when
        settingsActivity.onSharedPreferenceChanged(null, Settings.KEY_SYNC);

        // then
        verify(synchronization).syncPeriodic(syncInterval);
    }
}