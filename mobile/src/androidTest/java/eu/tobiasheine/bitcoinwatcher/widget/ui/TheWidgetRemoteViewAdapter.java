package eu.tobiasheine.bitcoinwatcher.widget.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.test.AndroidTestCase;
import android.widget.RemoteViews;

import eu.tobiasheine.bitcoinwatcher.R;
import eu.tobiasheine.bitcoinwatcher.price_sync.SyncService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TheWidgetRemoteViewAdapter extends AndroidTestCase {

    private final WidgetRemoteViewAdapter adapter = new WidgetRemoteViewAdapter();

    public void testAdaptsViewModelToRemoteViews() throws Exception {

        // given
        final RemoteViews remoteViews = mock(RemoteViews.class);
        final BitcoinWatcherWidgetViewModel viewModel = new BitcoinWatcherWidgetViewModel("100.00", "01.01.2015");
        final PendingIntent startSyncIntent = PendingIntent.getService(getContext(), 0, new Intent(getContext(), SyncService.class), 0);

        // when
        adapter.getRemoteViews(getContext(), remoteViews, viewModel);

        // then
        verify(remoteViews).setTextViewText(R.id.tvCurrentPrice, viewModel.getCurrentPrice());
        verify(remoteViews).setTextViewText(R.id.tvUpdatedAt, viewModel.getUpdatedAt());
        verify(remoteViews).setTextViewText(R.id.tvWidgetTitle, getContext().getText(R.string.widget_title));
        verify(remoteViews).setOnClickPendingIntent(R.id.btSync, startSyncIntent);
    }
}