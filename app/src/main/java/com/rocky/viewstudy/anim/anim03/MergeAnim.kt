package com.rocky.viewstudy.anim.anim03

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.animation.addListener

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/22
 * </pre>
 */
class MergeAnim(private val mergeAnimEnd:AnimEnd?) : LoadingState {
    private var currentRadius = mRadius
    private var currentAngle: Float = 0f

    override fun onDraw(view: View, canvas: Canvas, mPaint: Paint) {
        canvas.drawColor(Color.WHITE)
        val angle = (2 * Math.PI / colors.size)
        val width = view.width
        val height = view.height
        colors.forEachIndexed { index, color ->
            mPaint.color = view.resources.getColor(color)
            val a = currentAngle + angle + index * angle
            canvas.drawCircle(width / 2 + currentRadius * Math.cos(a).toFloat(),
                height / 2 + currentRadius * Math.sin(a).toFloat(), mCircleRadius.toFloat(), mPaint)
        }
        animExecute(view)
    }

    private var mergeAnim: ValueAnimator? = null

    private fun animExecute(view: View) {
        if (null == mergeAnim) {
            mergeAnim = ObjectAnimator.ofFloat(mRadius.toFloat(), 0f)
            mergeAnim?.run {
                duration = rotateDuration / 2
                addUpdateListener {
                    currentRadius = (it.animatedValue as Float).toInt()
                    view.invalidate()
                }
                interpolator = AnticipateInterpolator(5f)
                addListener(
                    onEnd = {
                        mergeAnim?.cancel()
                        mergeAnimEnd?.onEnd()
                    }
                )
                start()
            }
        }

    }


}