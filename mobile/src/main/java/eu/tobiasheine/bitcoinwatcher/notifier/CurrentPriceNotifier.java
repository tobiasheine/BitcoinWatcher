package eu.tobiasheine.bitcoinwatcher.notifier;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import eu.tobiasheine.bitcoinwatcher.R;
import eu.tobiasheine.bitcoinwatcher.model.CurrentPrice;

public class CurrentPriceNotifier {

    private final NotificationCompat.Builder builder;
    private final NotificationManager notificationManager;

    public CurrentPriceNotifier(Context context) {
        builder = new NotificationCompat.Builder(context);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notifyAboutNewCurrentPrice(final CurrentPrice currentPrice) {
        builder.setContentTitle("Bitcoin price update")
                .setContentText("New Price: " + currentPrice.getBpi().getEur().rate)
                .setSmallIcon(R.drawable.ic_launcher)
                .setVibrate(new long[] {0, 1000, 50, 2000});

        notificationManager.notify(1, builder.build());
    }
}
