package com.rocky.viewstudy.anim.anim03

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import java.security.spec.PSSParameterSpec.DEFAULT
import javax.crypto.spec.PSource.PSpecified.DEFAULT
import kotlin.math.max
import kotlin.math.sqrt

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/22
 * </pre>
 */
class ExpendAnim(private val expendAnimEnd: AnimEnd?) : LoadingState {
    private var maxRadius = 0f
    private var innerRadius = 0f

    override fun onDraw(view: View, canvas: Canvas, mPaint: Paint) {
        //此时 是画个白色圆环
        animExecute(view)
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = (maxRadius - innerRadius)
        canvas.drawCircle(view.width / 2f,
            view.height / 2f,
            innerRadius + (maxRadius - innerRadius) / 2,
            mPaint)

    }

    private var expendAnim: ValueAnimator? = null
    private fun animExecute(view: View) {
        if (null == expendAnim) {
            val width = view.width
            val height = view.height
            maxRadius =
                sqrt(width.toFloat() * width.toFloat() + height.toFloat() * height.toFloat()) / 2
            expendAnim = ObjectAnimator.ofFloat(maxRadius, 0f)
            expendAnim?.run {
                duration = rotateDuration / 2
                interpolator = LinearInterpolator()
                addUpdateListener {
                    innerRadius = maxRadius - (it.animatedValue as Float)
                    view.invalidate()
                }
                addListener(
                    onEnd = {
                        expendAnimEnd?.onEnd()
                    }
                )
                start()
            }
        }
    }
}