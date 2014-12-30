package eu.tobiasheine.bitcoinwatcher;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.SurfaceHolder;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import eu.tobiasheine.bitcoinwatcher.ui.WatchFaceViewModel;
import eu.tobiasheine.bitcoinwatcher.ui.WatchFaceViewModelFactory;

public class BitcoinWatchFaceService extends CanvasWatchFaceService {

    private static final Typeface NORMAL_TYPEFACE =
            Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    private static final long UPDATE_RATE_MS = TimeUnit.MINUTES.toMillis(1);
    private static final int MSG_UPDATE_TIME = 0;

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

        private Paint backgroundPaint;
        private Paint timePaint;
        private Paint bitcoinPricePaint;

        private Time time;

        private float distanceHourMinute;

        private int ambientBgColor;
        private int interactiveBgColor;

        private Bitmap bitcoinIcon;
        private int bitcoinIconSize;

        private WatchFaceViewModelFactory viewModelFactory;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(BitcoinWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());
            Resources resources = BitcoinWatchFaceService.this.getResources();

            distanceHourMinute = resources.getDimension(R.dimen.time_distance);

            backgroundPaint = new Paint();

            ambientBgColor = Color.parseColor("black");
            interactiveBgColor = getResources().getColor(android.R.color.holo_orange_light);

            backgroundPaint.setColor(ambientBgColor);

            timePaint = createTextPaint(Color.parseColor("white"), Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
            timePaint.setTextSize(resources.getDimension(R.dimen.time_text_size));

            bitcoinPricePaint = createTextPaint(Color.parseColor("white"));
            bitcoinPricePaint.setTextSize(resources.getDimension(R.dimen.bitcoin_text_size));

            time = new Time();

            final Bitmap unscaledIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            bitcoinIconSize = (int) getResources().getDimension(R.dimen.bitcoin_icon_size);

            bitcoinIcon = Bitmap.createScaledBitmap(unscaledIcon, bitcoinIconSize, bitcoinIconSize, false);

            viewModelFactory = new WatchFaceViewModelFactory(new Storage(getBaseContext()));
        }

        /* handler to update the time once a minute in interactive mode */
        final Handler updateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case MSG_UPDATE_TIME:
                        invalidate();
                        if (!isInAmbientMode()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs = UPDATE_RATE_MS
                                    - (timeMs % UPDATE_RATE_MS);
                            updateTimeHandler
                                    .sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                        }
                        break;
                }
            }
        };

        private Paint createTextPaint(int defaultInteractiveColor) {
            return createTextPaint(defaultInteractiveColor, NORMAL_TYPEFACE);
        }

        private Paint createTextPaint(int defaultInteractiveColor, Typeface typeface) {
            Paint paint = new Paint();
            paint.setColor(defaultInteractiveColor);
            paint.setTypeface(typeface);
            paint.setAntiAlias(true);
            return paint;
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

            updateTimer();
        }

        private void updateTimer() {
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (!isInAmbientMode()) {
                updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
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
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
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

            // draw background
            backgroundPaint.setColor(isInAmbientMode() ? ambientBgColor : interactiveBgColor);
            canvas.drawRect(0, 0, bounds.width(), bounds.height(), backgroundPaint);

            // draw time
            int centerX = bounds.centerX();
            int centerY = bounds.centerY();

            Rect hourBounds = new Rect();
            timePaint.getTextBounds(viewModel.hourString, 0, viewModel.hourString.length(), hourBounds);

            Rect minuteBounds = new Rect();
            timePaint.getTextBounds(viewModel.minuteString, 0, viewModel.minuteString.length(), minuteBounds);

            Rect bitcoinPriceBounds = new Rect();
            bitcoinPricePaint.getTextBounds(viewModel.bitcoinPriceString, 0, viewModel.bitcoinPriceString.length(), bitcoinPriceBounds);

            int timeX = centerX - minuteBounds.width() / 2;
            float hourY = centerY - distanceHourMinute / 2;
            float minuteY = centerY + minuteBounds.height() + distanceHourMinute / 2;

            float bitcoinIconX = centerX - (bitcoinPriceBounds.width() + bitcoinIconSize) / 2;

            canvas.drawText(viewModel.hourString, timeX, hourY, timePaint);
            canvas.drawText(viewModel.minuteString, timeX, minuteY, timePaint);

            // draw bitcoin price
            float magicalOffsetToAlignTextWithIcon = distanceHourMinute / 2;
            canvas.drawBitmap(bitcoinIcon, bitcoinIconX, bounds.bottom - bitcoinIconSize*2 + magicalOffsetToAlignTextWithIcon, new Paint());
            canvas.drawText(viewModel.bitcoinPriceString, bitcoinIconX + bitcoinIconSize, bounds.bottom - bitcoinIconSize, bitcoinPricePaint);
        }
    }
}
