package eu.tobiasheine.bitcoinwatcher.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.test.AndroidTestCase;
import android.widget.RemoteViews;

import eu.tobiasheine.bitcoinwatcher.R;
import eu.tobiasheine.bitcoinwatcher.dao.storage.Storage;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;
import eu.tobiasheine.bitcoinwatcher.widget.ui.BitcoinWatcherWidgetViewModel;
import eu.tobiasheine.bitcoinwatcher.widget.ui.WidgetRemoteViewAdapter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TheBitcoinWatcherWidgetProvider extends AndroidTestCase {

    private final BitcoinWatcherWidgetProvider appWidgetProvider = new BitcoinWatcherWidgetProvider();

    private WidgetRemoteViewAdapter remoteViewAdapter;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        Settings settings = mock(Settings.class);
        Storage storage = mock(Storage.class);
        remoteViewAdapter = mock(WidgetRemoteViewAdapter.class);

        appWidgetProvider.setSettings(settings);
        appWidgetProvider.setStorage(storage);
        appWidgetProvider.setRemoteViewAdapter(remoteViewAdapter);
    }

    public void testUpdatesAllWidgets() throws Exception {

        // given
        final Context context = getContext();
        final AppWidgetManager appWidgetManager = mock(AppWidgetManager.class);
        int firstWidgetId = 0;
        int secondWidgetId = 1;
        final int[] appWidgetIds = {firstWidgetId, secondWidgetId};

        // when
        appWidgetProvider.onUpdate(context, appWidgetManager, appWidgetIds);

        // then
        verify(appWidgetManager).updateAppWidget(eq(firstWidgetId), any(RemoteViews.class));
        verify(appWidgetManager).updateAppWidget(eq(secondWidgetId), any(RemoteViews.class));
    }

    public void testUpdatesWidgetWithRemoteViews() throws Exception {
        // given
        final RemoteViews remoteViews = new RemoteViews(getContext().getPackageName(), R.layout.bitcoin_watcher_appwidget);
        when(remoteViewAdapter.getRemoteViews(any(Context.class), any(RemoteViews.class), any(BitcoinWatcherWidgetViewModel.class))).thenReturn(remoteViews);

        final Context context = getContext();
        final AppWidgetManager appWidgetManager = mock(AppWidgetManager.class);
        int widgetId = 0;

        final int[] appWidgetIds = {widgetId};

        // when
        appWidgetProvider.onUpdate(context, appWidgetManager, appWidgetIds);

        // then
        verify(appWidgetManager).updateAppWidget(widgetId, remoteViews);

    }

}