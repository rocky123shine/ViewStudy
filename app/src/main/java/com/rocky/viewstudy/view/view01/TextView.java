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

        if (widthMode == MeasureSpec.AT_MOST)//自适应 按照父布局的宽度 自适应自身的宽
        {
            Rect bounds = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), bounds);
            width = bounds.width() + getPaddingStart() + getPaddingEnd();
        }

//             MeasureSpec.UNSPECIFIED://尽可能的大  一般是滑动布局使用 listview   scrollview 等的滑动方向
//                // scrollview + Listview 会显示不全 解决办法是更改 mode模式 因为 在scrollview 测量子view的时候 mode 就是 UNSPECIFIED
//                //但是 子view listview 又是UNSPECIFIED


        if (heightMode == MeasureSpec.AT_MOST)//自适应 按照父布局的宽度 自适应自身的宽
        {
            Rect bounds = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), bounds);
            height = bounds.height() + getPaddingTop() + getPaddingBottom();
        }


        setMeasuredDimension(width, height);
    }

    //viewgroup 默认不会调用onDraw
    //解决办法 1  调用这个方法 dispatchDraw()
    //2.         setWillNotDraw(false);
    //判断是否设置了背景  未设置  设置个 透明背景
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
