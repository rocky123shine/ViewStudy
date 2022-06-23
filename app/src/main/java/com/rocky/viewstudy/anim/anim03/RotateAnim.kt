package com.rocky.viewstudy.anim.anim03

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.animation.LinearInterpolator
import com.rocky.viewstudy.R
import com.rocky.viewstudy.dp

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/22
 * </pre>
 */
class RotateAnim : LoadingState {
    private var currentAngle: Float = 0f

    override fun onDraw(view: View, canvas: Canvas, mPaint: Paint) {
        canvas.drawColor(Color.WHITE)
        val angle = (2 * Math.PI / colors.size)
        val width = view.width
        val height = view.height
        colors.forEachIndexed { index, color ->
            mPaint.color = view.resources.getColor(color)
            val a = currentAngle + angle + index * angle
            canvas.drawCircle(width / 2 + mRadius * Math.cos(a).toFloat(),
                height / 2 + mRadius * Math.sin(a).toFloat(), mCircleRadius.toFloat(), mPaint)
        }
        animExecute(view)

    }

    private var rotateAnim: ValueAnimator? = null
    private fun animExecute(view: View) {
        if (null == rotateAnim) {
            rotateAnim = ObjectAnimator.ofFloat(0f, 2 * Math.PI.toFloat())

            rotateAnim?.run {
                duration = rotateDuration
                addUpdateListener {
                    currentAngle = (it.animatedValue as Float)
                    view.invalidate()
                }
                repeatCount = -1
                //使用插值器 实现 停止旋转的时候 放大
                interpolator = LinearInterpolator()
                start()
            }
        }

    }

    fun cancel() {
        rotateAnim?.cancel()
    }

}