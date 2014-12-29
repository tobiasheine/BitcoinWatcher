package eu.tobiasheine.bitcoinwatcher.notifier;

import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import eu.tobiasheine.bitcoinwatcher.R;
import eu.tobiasheine.bitcoinwatcher.model.CurrentPrice;
import eu.tobiasheine.bitcoinwatcher.widget.BitcoinWatcherWidgetProvider;

public class CurrentPriceNotifier {

    private final NotificationCompat.Builder builder;
    private final NotificationManager notificationManager;

    private final Context context;

    public CurrentPriceNotifier(Context context) {
        this.builder = new NotificationCompat.Builder(context);
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.context = context;
    }

    public void notifyAboutNewCurrentPrice(final CurrentPrice currentPrice) {
        builder.setContentTitle("Bitcoin price update")
                .setContentText("New Price: " + currentPrice.getBpi().getEur().rate)
                .setSmallIcon(R.drawable.ic_launcher)
                .setVibrate(new long[] {0, 1000, 50, 2000});

        notificationManager.notify(1, builder.build());
    }

    public void notifyWidget() {
        Intent intent = new Intent(context, BitcoinWatcherWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, BitcoinWatcherWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        context.sendBroadcast(intent);
    }
}
