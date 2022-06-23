package com.rocky.viewstudy.view.vg08;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import static android.os.Build.VERSION.SDK_INT;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/13
 *     des    : 实现步骤
 *                      1. 子view 只能是俩个 且只有第二个可以拖动
 *                      2. 可拖动的边界限定
 *                      3. 拖拽松手后 计算位置 自动滑动
 *                      4. 拖动view 换成listview
 *                      5. 子view 事件拦截 交给父类处理
 * </pre>
 */
public class VerticalDragListView extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private View mDragView;
    private int mMaxDragHeight;
    private boolean isOpen;

    public VerticalDragListView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewDragHelper = ViewDragHelper.create(this, mViewDragHelperCallback);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            try {
                throw new Exception("子view 必须为两个。。。。。。");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mDragView = getChildAt(1);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取到第一个子view 的高度
        mMaxDragHeight = getChildAt(0).getMeasuredHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }


    private float mDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

//        //此时拦截事件 让父类处理 执行拖拽
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mViewDragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:

                if (isOpen) {

                    if (ev.getY() - mDownY > 0) {
                        //向下滑动
                        return super.onInterceptTouchEvent(ev);
                    } else {
                        //向上滑动
                        return true;
                    }

                } else {
                    if (ev.getY() - mDownY > 0) {
                        //向下滑动
                        if (canChildScrollUp()) {
                            return super.onInterceptTouchEvent(ev);
                        } else {
                            return true;
                        }

                    } else {
                        //向上滑动
                        return super.onInterceptTouchEvent(ev);
                    }
                }

        }

        return super.onInterceptTouchEvent(ev);
    }

    //拖动子view
    private ViewDragHelper.Callback mViewDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            //指定子view 是否可以拖动

            //控制可以推动的view
            return mDragView == child;
        }


        //拖动方向只要求是垂直方向
        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            //垂直拖动移动的位置
            //限制拖动的边界 在第一个子view的高度范围内
            if (top < 0) {
                top = 0;
            }
            if (top > mMaxDragHeight) {
                top = mMaxDragHeight;
            }

            return top;
        }

//        @Override
//        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
//            //水平拖动移动的位置
//            return left;
//        }


        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //此时是拖拽的view 松手后执行

            if (mDragView == releasedChild) {
                //计算 拖动的高度
                if (mDragView.getTop() > mMaxDragHeight / 2) {
                    //打开
                    mViewDragHelper.settleCapturedViewAt(0, mMaxDragHeight);
                    isOpen = true;
                } else {
                    //关闭
                    mViewDragHelper.settleCapturedViewAt(0, 0);
                    isOpen = false;

                }
                invalidate();
            }

        }

    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    //判断子view 是否滚动到顶部
    public boolean canChildScrollUp() {
        if (SDK_INT < 14) {
            if (mDragView instanceof AbsListView) {
                AbsListView dragView = (AbsListView) mDragView;

                return dragView.getChildCount() > 0 && (
                        dragView.getFirstVisiblePosition() > 0 ||
                                dragView.getChildAt(0).getTop() < dragView.getPaddingTop()
                );
            } else {
                return ViewCompat.canScrollVertically(mDragView, -1) || mDragView.getScrollY() > 0;
            }
        } else {

            return ViewCompat.canScrollVertically(mDragView, -1);
        }

    }


}
