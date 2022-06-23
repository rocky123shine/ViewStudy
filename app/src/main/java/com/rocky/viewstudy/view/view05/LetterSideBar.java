package com.rocky.viewstudy.view.view05;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.rocky.viewstudy.R;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/02
 * </pre>
 */
public class LetterSideBar extends View {
    private Paint mPaint;
    private int originColor = Color.BLACK;
    private int selectColor = Color.RED;
    private int textSize = 12;
    private int itemHeight = 0;
    private String currentLetter;


    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LetterSideBar);

        originColor = array.getColor(R.styleable.LetterSideBar_normalColor, originColor);
        selectColor = array.getColor(R.styleable.LetterSideBar_selectColor, selectColor);
        textSize = array.getDimensionPixelSize(R.styleable.LetterSideBar_letterSize, spToPx(textSize));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setColor(originColor);
        array.recycle();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int letterWidth = (int) mPaint.measureText("A");
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int w = letterWidth + getPaddingStart() + getPaddingEnd();
        setMeasuredDimension(w, h);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / 26;

        //super.onDraw(canvas);
        for (char i = 'A'; i <= 'Z'; i++) {
            int centerY = (i - 'A') * itemHeight + itemHeight / 2 + getPaddingTop();
            Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
            int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
            int baseLine = centerY + dy;
            float letterWidth = mPaint.measureText(i + "");
            String letter = i + "";
            if (letter.equals(currentLetter)) {
                mPaint.setColor(selectColor);
            } else {
                mPaint.setColor(originColor);

            }
            canvas.drawText(letter, (getWidth() >> 1) - letterWidth / 2, baseLine, mPaint);
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float currentY = event.getY();
                int currentPos = (int) (currentY / itemHeight) - 1;
                if (currentPos < 0) currentPos = 0;
                if (currentPos > 26) currentPos = 26;

                currentLetter = String.valueOf((char) (currentPos + 'A'));
                if (changeListener != null) {
                    changeListener.onLetterChanged(currentLetter);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                currentLetter = "";
                if (changeListener != null) {
                    changeListener.onLetterChanged(currentLetter);
                }
                invalidate();
                break;

        }
        return true;

    }

    private int spToPx(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private IOnLetterSelectChangeListener changeListener;

    public void setChangeListener(IOnLetterSelectChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface IOnLetterSelectChangeListener {
        void onLetterChanged(String letter);
    }
}
