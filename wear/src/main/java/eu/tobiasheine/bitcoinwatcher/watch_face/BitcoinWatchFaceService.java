package eu.tobiasheine.bitcoinwatcher.watch_face;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.SurfaceHolder;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import java.util.TimeZone;

import eu.tobiasheine.bitcoinwatcher.price_sync.Storage;
import eu.tobiasheine.bitcoinwatcher.watch_face.ui.WatchFaceDrawer;
import eu.tobiasheine.bitcoinwatcher.watch_face.ui.WatchFaceTimer;
import eu.tobiasheine.bitcoinwatcher.watch_face.ui.WatchFaceViewModel;
import eu.tobiasheine.bitcoinwatcher.watch_face.ui.WatchFaceViewModelFactory;

public class BitcoinWatchFaceService extends CanvasWatchFaceService {

    private GoogleApiClient googleApiClient;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine {

        final BroadcastReceiver timeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                time.clear(intent.getStringExtra("time-zone"));
                time.setToNow();
            }
        };

        private boolean isTimeZoneReceiverRegistered = false;

        private final Time time = new Time();

        private WatchFaceViewModelFactory viewModelFactory;
        private WatchFaceTimer watchFaceTimer;
        private WatchFaceDrawer drawer;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(BitcoinWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());

            Resources resources = BitcoinWatchFaceService.this.getResources();


            googleApiClient = new GoogleApiClient.Builder(getBaseContext())
                    .addApi(Wearable.API)
                    .build();

            googleApiClient.connect();

            Storage storage = new Storage(getBaseContext());
            viewModelFactory = new WatchFaceViewModelFactory(storage, getBaseContext());

            watchFaceTimer = new WatchFaceTimer(this, storage, googleApiClient);

            drawer = new WatchFaceDrawer(resources);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                time.clear(TimeZone.getDefault().getID());
                time.setToNow();
            } else {
                unregisterReceiver();
            }

            watchFaceTimer.onWatchFaceVisibilityChange();
        }

        private void registerReceiver() {
            if (isTimeZoneReceiverRegistered) {
                return;
            }
            isTimeZoneReceiverRegistered = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            BitcoinWatchFaceService.this.registerReceiver(timeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!isTimeZoneReceiverRegistered) {
                return;
            }
            isTimeZoneReceiverRegistered = false;
            BitcoinWatchFaceService.this.unregisterReceiver(timeZoneReceiver);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            watchFaceTimer.onDestroy();

            if (googleApiClient.isConnected()) {
                googleApiClient.disconnect();
            }
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            invalidate();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            time.setToNow();

            final WatchFaceViewModel viewModel = viewModelFactory.createViewModel(time);

            drawer.drawViewModelOnCanvas(viewModel, canvas, bounds, this);
        }
    }
}
