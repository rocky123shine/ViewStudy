package com.rocky.viewstudy.anim.anim03

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/21
 *     des    :第一步 绘制彩色圆
 *             第二部 旋转彩色圆
 *             第三部 收缩
 *             第四部 展开
 * </pre>
 */
class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), AnimEnd {
    private var dispatchEvent: Boolean = false //当执行完动之后 分发事件
    private val mPaint: Paint = Paint()
    private var currentState: LoadingState

    init {
        mPaint.isAntiAlias = true
        // mPaint.isDither = true
        currentState = RotateAnim()
        dispatchEvent = false

    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        currentState.onDraw(this, canvas, mPaint = mPaint)
    }

    public fun disappear() {
        if (currentState is RotateAnim) {
            (currentState as RotateAnim).cancel()
        }
        currentState = MergeAnim(this)
        invalidate()
    }

    //merge 之后  执行expend
    override fun onEnd() {
        if (currentState !is ExpendAnim) {
            currentState = ExpendAnim(this)
            invalidate()
        } else {
            //此时 整个动画结束
            dispatchEvent = true
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (dispatchEvent) {
            return super.onTouchEvent(event)
        }
        return true
    }

}