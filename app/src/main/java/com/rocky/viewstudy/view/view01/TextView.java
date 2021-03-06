package com.rocky.viewstudy.view.view01;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.rocky.viewstudy.R;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/05/30
 * </pre>
 */
public class TextView extends LinearLayout {
    private String mText;
    private int mTextSize = 12;
    private int mTextColor = Color.BLACK;
    private Paint mPaint;

    public TextView(Context context) {
        this(context, null);
    }

    public TextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MTextView);

        mText = array.getString(R.styleable.MTextView_text);
        mTextColor = array.getColor(R.styleable.MTextView_textColor, mTextColor);
        mTextSize = array.getDimensionPixelSize(R.styleable.MTextView_textSize, spToPx(mTextSize));

        array.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);


        //setWillNotDraw(false);

        setBackgroundColor(Color.TRANSPARENT);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST)//????????? ???????????????????????? ?????????????????????
        {
            Rect bounds = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), bounds);
            width = bounds.width() + getPaddingStart() + getPaddingEnd();
        }

//             MeasureSpec.UNSPECIFIED://???????????????  ??????????????????????????? listview   scrollview ??????????????????
//                // scrollview + Listview ??????????????? ????????????????????? mode?????? ?????? ???scrollview ?????????view????????? mode ?????? UNSPECIFIED
//                //?????? ???view listview ??????UNSPECIFIED


        if (heightMode == MeasureSpec.AT_MOST)//????????? ???????????????????????? ?????????????????????
        {
            Rect bounds = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), bounds);
            height = bounds.height() + getPaddingTop() + getPaddingBottom();
        }


        setMeasuredDimension(width, height);
    }

    //viewgroup ??????????????????onDraw
    //???????????? 1  ?????????????????? dispatchDraw()
    //2.         setWillNotDraw(false);
    //???????????????????????????  ?????????  ????????? ????????????
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextSize(mTextSize);

        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float dy = (metrics.bottom - metrics.top) / 2 - metrics.bottom;
        float baseLine = getHeight() / 2.f + dy;
        canvas.drawText(mText, getPaddingStart(), baseLine, mPaint);
    }

//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        mPaint.setTextSize(mTextSize);
//
//        Paint.FontMetrics metrics = mPaint.getFontMetrics();
//        float dy = (metrics.bottom - metrics.top) / 2 - metrics.bottom;
//        float baseLine = getHeight() / 2.f + dy;
//        canvas.drawText(mText, getPaddingStart(), baseLine, mPaint);
//
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    private int spToPx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}
