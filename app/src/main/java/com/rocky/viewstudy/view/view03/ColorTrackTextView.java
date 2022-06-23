package com.rocky.viewstudy.view.view03;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.rocky.viewstudy.R;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/01
 * </pre>
 */
public class ColorTrackTextView extends androidx.appcompat.widget.AppCompatTextView {
    private Paint mOriginPaint;
    private Paint mChangePaint;

    private float mCurrentProgress = 0.5f;


    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint(context, attrs);
    }

    private void initPaint(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        int changeColor = array.getColor(R.styleable.ColorTrackTextView_changeColor, Color.BLACK);
        int originColor = array.getColor(R.styleable.ColorTrackTextView_originColor, Color.BLACK);

        mOriginPaint = getPaintByColor(originColor);
        mChangePaint = getPaintByColor(changeColor);

        array.recycle();

    }

    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(getTextSize());
        return paint;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        int middle = (int) (mCurrentProgress * getWidth());
        dratText(canvas,mOriginPaint,0,middle);
        dratText(canvas,mChangePaint,middle,getWidth());
    }

    private void dratText(Canvas canvas, Paint paint, int start, int end) {
        canvas.save();
        canvas.clipRect(start, 0, end, getHeight());
        String text = getText().toString();
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        int x = getWidth() / 2 - rect.width() / 2;
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, x, baseLine, paint);
        canvas.restore();
    }
}
