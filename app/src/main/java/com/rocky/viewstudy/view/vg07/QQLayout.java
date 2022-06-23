package com.rocky.viewstudy.view.vg07;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
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
public class QQLayout extends HorizontalScrollView {
    private int minContentWidth = 62;
    private int mMenuWidth = 0;
    private View contentView;
    private View menuView;
    private boolean mMenuIsOpen;

    public QQLayout(Context context) {
        this(context,null);
    }

    public QQLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SlidingLayout);
        minContentWidth = array.getDimensionPixelSize(R.styleable.SlidingLayout_minContentWidth, dp2px(minContentWidth));
        array.recycle();
    }

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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
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

}
