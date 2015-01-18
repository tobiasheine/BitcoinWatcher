package eu.tobiasheine.bitcoinwatcher.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import javax.inject.Inject;

import eu.tobiasheine.bitcoinwatcher.BitcoinWatcherApplication;
import eu.tobiasheine.bitcoinwatcher.R;
import eu.tobiasheine.bitcoinwatcher.dao.storage.IStorage;
import eu.tobiasheine.bitcoinwatcher.dao.storage.Storage;
import eu.tobiasheine.bitcoinwatcher.di.Dependencies;
import eu.tobiasheine.bitcoinwatcher.settings.ISettings;
import eu.tobiasheine.bitcoinwatcher.settings.Settings;
import eu.tobiasheine.bitcoinwatcher.widget.ui.BitcoinWatcherViewModelFactory;
import eu.tobiasheine.bitcoinwatcher.widget.ui.BitcoinWatcherWidgetViewModel;
import eu.tobiasheine.bitcoinwatcher.widget.ui.WidgetRemoteViewAdapter;

public class BitcoinWatcherWidgetProvider extends AppWidgetProvider {

    @Inject
    ISettings settings;

    @Inject
    IStorage storage;

    private WidgetRemoteViewAdapter remoteViewAdapter;

    public BitcoinWatcherWidgetProvider() {
        super();
        BitcoinWatcherApplication.getDependencies().inject(this);
        remoteViewAdapter = new WidgetRemoteViewAdapter();
    }

    public void setSettings(final Settings settings) {
        this.settings = settings;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void setRemoteViewAdapter(WidgetRemoteViewAdapter remoteViewAdapter) {
        this.remoteViewAdapter = remoteViewAdapter;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final BitcoinWatcherWidgetViewModel viewModel = BitcoinWatcherViewModelFactory.create(context, settings, storage.getStoredPrice());
        final RemoteViews views = remoteViewAdapter.getRemoteViews(context, new RemoteViews(context.getPackageName(), R.layout.bitcoin_watcher_appwidget), viewModel);

        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
