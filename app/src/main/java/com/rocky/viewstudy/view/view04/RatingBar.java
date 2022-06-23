package com.rocky.viewstudy.view.view04;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.rocky.viewstudy.R;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/01
 * </pre>
 */
public class RatingBar extends View {

    private Bitmap mStarNormalBitmap;
    private Bitmap mFocusBitmap;
    private int gradeNum = 5;
    private final int SPACE = 0;
    private int currentNum = 0;


    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        gradeNum = array.getInt(R.styleable.RatingBar_gradeNum, gradeNum);
        int startNormalId = array.getResourceId(R.styleable.RatingBar_startNormal, 0);
        int starFocusId = array.getResourceId(R.styleable.RatingBar_starFocus, 0);
        if (startNormalId == 0) {
            throw new RuntimeException("请设置属性 startNormal");
        }
        if (starFocusId == 0) {
            throw new RuntimeException("请设置属性 starFocus");
        }

        mStarNormalBitmap = BitmapFactory.decodeResource(getResources(), startNormalId);
        mFocusBitmap = BitmapFactory.decodeResource(getResources(), starFocusId);

        array.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = mFocusBitmap.getHeight();
        int width = mFocusBitmap.getWidth() * 5 + SPACE * 4;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < gradeNum; i++) {
            int x = i * mStarNormalBitmap.getWidth() + i * SPACE;

            canvas.drawBitmap(i > currentNum ?mStarNormalBitmap:mFocusBitmap, x, 0, null);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                int currentGradle = (int) (x / mFocusBitmap.getWidth());
                if (currentGradle < 0) currentGradle = 0;
                if (currentGradle > gradeNum) currentGradle = gradeNum;
                currentNum = currentGradle;
                invalidate();
        }
        return true;
    }
}
