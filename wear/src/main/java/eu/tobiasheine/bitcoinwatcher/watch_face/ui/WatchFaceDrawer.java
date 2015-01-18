package eu.tobiasheine.bitcoinwatcher.watch_face.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.wearable.watchface.CanvasWatchFaceService;

import eu.tobiasheine.bitcoinwatcher.R;

public class WatchFaceDrawer {

    private static final Typeface NORMAL_TYPEFACE =
            Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    private final Paint backgroundPaint;
    private final Paint timePaint;
    private final Paint bitcoinPricePaint;

    private final int ambientBgColor;
    private final int interactiveBgColor;

    private final Bitmap bitcoinIcon;
    private final int bitcoinIconSize;

    private final float distanceHourMinute;

    public WatchFaceDrawer(final Resources resources) {
        distanceHourMinute = resources.getDimension(R.dimen.time_distance);

        backgroundPaint = new Paint();

        ambientBgColor = Color.parseColor("black");
        interactiveBgColor = resources.getColor(android.R.color.holo_orange_light);

        backgroundPaint.setColor(ambientBgColor);

        timePaint = createTextPaint(Color.parseColor("white"), Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        timePaint.setTextSize(resources.getDimension(R.dimen.time_text_size));

        bitcoinPricePaint = createTextPaint(Color.parseColor("white"));
        bitcoinPricePaint.setTextSize(resources.getDimension(R.dimen.bitcoin_text_size));

        final Bitmap unscaledIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
        bitcoinIconSize = (int) resources.getDimension(R.dimen.bitcoin_icon_size);

        bitcoinIcon = Bitmap.createScaledBitmap(unscaledIcon, bitcoinIconSize, bitcoinIconSize, false);
    }

    public void drawViewModelOnCanvas(final WatchFaceViewModel viewModel, final Canvas canvas, final Rect bounds, final CanvasWatchFaceService.Engine engine) {
        // draw background
        backgroundPaint.setColor(engine.isInAmbientMode() ? ambientBgColor : interactiveBgColor);
        canvas.drawRect(0, 0, bounds.width(), bounds.height(), backgroundPaint);

        // draw time
        int baseX = bounds.centerX();
        int baseY = bounds.centerY();
        baseY -= distanceHourMinute*2;

        Rect hourBounds = new Rect();
        timePaint.getTextBounds(viewModel.hourString, 0, viewModel.hourString.length(), hourBounds);

        Rect minuteBounds = new Rect();
        timePaint.getTextBounds(viewModel.minuteString, 0, viewModel.minuteString.length(), minuteBounds);

        Rect bitcoinPriceBounds = new Rect();
        bitcoinPricePaint.getTextBounds(viewModel.bitcoinPriceString, 0, viewModel.bitcoinPriceString.length(), bitcoinPriceBounds);

        int timeX = baseX - minuteBounds.width() / 2;
        float hourY = baseY - distanceHourMinute / 2;
        float minuteY = baseY + minuteBounds.height() + distanceHourMinute / 2;

        float bitcoinIconX = baseX - (bitcoinPriceBounds.width() + bitcoinIconSize) / 2;

        canvas.drawText(viewModel.hourString, timeX, hourY, timePaint);
        canvas.drawText(viewModel.minuteString, timeX, minuteY, timePaint);

        // draw bitcoin price
        float magicalOffsetToAlignTextWithIcon = distanceHourMinute / 2;
        int baseYPrice = bounds.bottom;
        baseYPrice -= distanceHourMinute*2;
        canvas.drawBitmap(bitcoinIcon, bitcoinIconX, baseYPrice - bitcoinIconSize * 2 + magicalOffsetToAlignTextWithIcon, new Paint());
        canvas.drawText(viewModel.bitcoinPriceString, bitcoinIconX + bitcoinIconSize, baseYPrice - bitcoinIconSize, bitcoinPricePaint);
    }

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
}
