package com.rocky.viewstudy.view.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.rocky.viewstudy.R;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/13
 * </pre>
 */
public class SlidingLayout extends HorizontalScrollView {
    private int minContentWidth = 62;
    private int mMenuWidth = 0;
    private View contentView;
    private View menuView;
    //处理快速滑动
    //手势处理类
    private GestureDetector mGestureDetector;
    private boolean mMenuIsOpen;
    private boolean isIntercept;

    public SlidingLayout(Context context) {
        this(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SlidingLayout);
        minContentWidth = array.getDimensionPixelSize(R.styleable.SlidingLayout_minContentWidth, dp2px(minContentWidth));
        array.recycle();
        mGestureDetector = new GestureDetector(context, mGestureListener);


    }

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //处理款速滑动
            //System.out.println("velocityX-------->"+velocityX);

            if (mMenuIsOpen) {
                if (velocityX < 0) {
                    closeMenu();
                    return true;
                }
            } else {
                if (velocityX > 0) {
                    openMenu();
                    return true;
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LinearLayout viewContainer = (LinearLayout) getChildAt(0);
        if (viewContainer.getChildCount() != 2) {
            try {
                throw new Exception("子view 必须为2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        menuView = viewContainer.getChildAt(0);
        ViewGroup.LayoutParams menuViewLayoutParams = menuView.getLayoutParams();
        mMenuWidth = getWindowWidth() - minContentWidth;
        menuViewLayoutParams.width = mMenuWidth;
        menuView.setLayoutParams(menuViewLayoutParams);

        contentView = viewContainer.getChildAt(1);
        ViewGroup.LayoutParams contentViewLayoutParams = contentView.getLayoutParams();
        contentViewLayoutParams.width = getWindowWidth();
        contentView.setLayoutParams(contentViewLayoutParams);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollTo(mMenuWidth, 0);
        mMenuIsOpen = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isIntercept) {
            return true;
        }

        if (mGestureDetector.onTouchEvent(ev)) {
            return true;
        }
        if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
            //处理手指抬起
            float mCurrentX = getScrollX();
            if (mCurrentX > mMenuWidth / 2) {
                closeMenu();
            } else {
                openMenu();
            }
//            menuView.setZ(0);
            return true;
        }
//        menuView.setZ(2);
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // System.out.println("l----------->"+l);
        // l 最大值为menu的宽度  这时我们可以计算一个缩放系数
        float scale = 1f * l / mMenuWidth;
        float rightScale = 0.7f + 0.3f * scale;
        contentView.setPivotX(0);
        contentView.setPivotY(getMeasuredHeight() / 2);
        contentView.setScaleX(rightScale);
        contentView.setScaleY(rightScale);
        //设置 菜单的透明度
        float alph = 0.5f + 0.5f * (1 - rightScale);
        menuView.setAlpha(alph);
        float leftScale = 0.7f + 0.3f * (1 - scale);
        menuView.setPivotX(mMenuWidth);
        menuView.setPivotY(getMeasuredHeight() / 2);
        menuView.setScaleX(leftScale);
        menuView.setScaleY(leftScale);
//

        menuView.setTranslationX(0.25f*l);

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        isIntercept = false;
        if (mMenuIsOpen) {
            float x = ev.getX();
            if (x > mMenuWidth) {
                isIntercept = true;
                //拦截子view的时间
                closeMenu();
                //此时 拦截了子view 的事件  但是会走自己的onTouch事件
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void openMenu() {
        smoothScrollTo(0, 0);

        mMenuIsOpen = true;
    }

    private void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
        mMenuIsOpen = false;
    }

    private int getWindowHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    private int getWindowWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
