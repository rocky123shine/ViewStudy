package com.rocky.viewstudy.anim.anim03

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.rocky.viewstudy.R
import com.rocky.viewstudy.dp

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/22
 * </pre>
 */
interface LoadingState {
    val mRadius: Int
        get() = 80.dp
    val mCircleRadius: Int
        get() = 20.dp

    val rotateDuration: Long //旋转时间
        get() = 3000
    val colors: Array<Int>
        get() = arrayOf(
            R.color.purple_200,
            R.color.purple_500,
            R.color.purple_700,
            R.color.triangle,
            R.color.circle,
            R.color.rect)

    fun onDraw(view: View, canvas: Canvas, mPaint: Paint)

}