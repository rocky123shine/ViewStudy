package com.rocky.viewstudy.view.view02;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.rocky.viewstudy.R;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/05/31
 * </pre>
 */
public class QQStepView extends View {
    private int mInnerColor = Color.BLACK;
    private int mOutColor = Color.BLACK;
    private int mStepTextColor = Color.BLACK;

    private int mTextSize = 12;
    private int mBorderWidth = 20;
    private Paint mOutPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;

    private int mStepMax = 100;//总步长
    private int mCurrentStep = 60;//当前步数


    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mInnerColor = array.getColor(R.styleable.QQStepView_innerColor, mInnerColor);
        mOutColor = array.getColor(R.styleable.QQStepView_outColor, mOutColor);
        mStepTextColor = array.getColor(R.styleable.QQStepView_stepTextColor, mStepTextColor);
        mTextSize = array.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, transform(TypedValue.COMPLEX_UNIT_SP, mTextSize));
        mBorderWidth = array.getDimensionPixelSize(R.styleable.QQStepView_borderWidth, mBorderWidth);
        array.recycle();
        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setColor(mOutColor);
        mOutPaint.setStrokeWidth(mBorderWidth);
        mOutPaint.setStyle(Paint.Style.STROKE);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);


        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mTextSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST || MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            throw new IllegalArgumentException("请给出具体的宽高");
        }
        //宽高一直保证  取宽高最小值
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        int radius = center - mBorderWidth / 2;

        RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(rectF, 135, 270, false, mOutPaint);
        if (mStepMax == 0) return;
        canvas.drawArc(rectF, 135, mCurrentStep * 270 / mStepMax, false, mInnerPaint);
        //中心挥之文字
        String stepText = mCurrentStep + "";
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(stepText, 0, stepText.length(), textBounds);

        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float dy = (metrics.bottom - metrics.top) / 2 - metrics.bottom;
        float baseLine = getHeight() / 2.f + dy;
        canvas.drawText(stepText, center - textBounds.width() / 2.f, baseLine, mTextPaint);


    }

    public void setStepMax(int stepMax) {
        this.mStepMax = stepMax;

    }

    public void setCurrentStep(int currentStep) {
        this.mCurrentStep = currentStep;
        invalidate();
    }

    private int transform(int unit, int sp) {
        return (int) TypedValue.applyDimension(unit, sp, getResources().getDisplayMetrics());
    }
}
