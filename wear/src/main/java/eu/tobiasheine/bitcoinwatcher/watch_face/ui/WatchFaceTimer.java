package eu.tobiasheine.bitcoinwatcher.watch_face.ui;


import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;


import com.google.android.gms.common.api.GoogleApiClient;

import java.util.concurrent.TimeUnit;

import eu.tobiasheine.bitcoinwatcher.price_sync.PullPriceTask;
import eu.tobiasheine.bitcoinwatcher.price_sync.Storage;

public class WatchFaceTimer extends Handler {

    private final CanvasWatchFaceService.Engine engine;
    private final Storage storage;
    private final GoogleApiClient googleApiClient;

    private static final int MSG_UPDATE_TIME = 0;
    private static final long UPDATE_RATE_MS = TimeUnit.MINUTES.toMillis(1);
    private PullPriceTask pullPriceTask = null;


    public WatchFaceTimer(CanvasWatchFaceService.Engine engine, Storage storage, GoogleApiClient googleApiClient) {
        this.engine = engine;
        this.storage = storage;

        this.googleApiClient = googleApiClient;
    }

    public void onWatchFaceVisibilityChange() {
        removeMessages(MSG_UPDATE_TIME);
        if (!engine.isInAmbientMode()) {
            sendEmptyMessage(MSG_UPDATE_TIME);
        }
    }

    public void onDestroy(){
        removeMessages(MSG_UPDATE_TIME);
    }

    @Override
    public void handleMessage(Message message) {

        switch (message.what) {
            case MSG_UPDATE_TIME:

                if (storage.getPrice() < 0) {
                    if (pullPriceTask != null) {
                        pullPriceTask.cancel(true);
                    }

                    pullPriceTask = new PullPriceTask(googleApiClient, storage);
                    pullPriceTask.execute();
                }

                engine.invalidate();
                if (!engine.isInAmbientMode()) {
                    long timeMs = System.currentTimeMillis();
                    long delayMs = UPDATE_RATE_MS
                            - (timeMs % UPDATE_RATE_MS);

                            sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                }
                break;
        }
    }
}
