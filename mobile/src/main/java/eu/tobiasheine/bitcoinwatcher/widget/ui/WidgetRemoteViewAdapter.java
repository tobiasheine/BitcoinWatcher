package eu.tobiasheine.bitcoinwatcher.widget.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import eu.tobiasheine.bitcoinwatcher.R;
import eu.tobiasheine.bitcoinwatcher.price_sync.SyncService;

public class WidgetRemoteViewAdapter {

    public RemoteViews getRemoteViews(final Context context,final RemoteViews remoteViews, final BitcoinWatcherWidgetViewModel viewModel) {

        remoteViews.setTextViewText(R.id.tvCurrentPrice, viewModel.getCurrentPrice());
        remoteViews.setTextViewText(R.id.tvUpdatedAt, viewModel.getUpdatedAt());
        remoteViews.setTextViewText(R.id.tvWidgetTitle, context.getText(R.string.widget_title));

        final PendingIntent startSyncIntent = PendingIntent.getService(context, 0, new Intent(context, SyncService.class), 0);
        remoteViews.setOnClickPendingIntent(R.id.btSync, startSyncIntent);

        return remoteViews;
    }
}
