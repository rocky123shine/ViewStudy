package com.rocky.viewstudy.anim.anim01

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.rocky.viewstudy.R
import java.lang.Math.*

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/17
 *     des    : 绘制 三角形  正方形和圆形
 * </pre>
 */
class ShapeView : View {
    var mCurrentShape: SHAPE = SHAPE.TRIANGLE
    private var mPaint: Paint = Paint()
    private val minWidth = 50

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context,
        attributeSet,
        defStyleAttr)

    init {
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var mWidth = MeasureSpec.getSize(widthMeasureSpec)
        var mHeight = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = min(max(mWidth, minWidth), max(mHeight, minWidth))
        setMeasuredDimension(mWidth, mWidth)

    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas ?: return
        val mWidth = width
        when (mCurrentShape) {
            SHAPE.CIRCLE -> {
                mPaint.color = ContextCompat.getColor(context, R.color.circle)
                canvas.drawCircle(mWidth / 2.0f, mWidth / 2.0f, mWidth / 2.0f, mPaint)
            }
            SHAPE.RECT -> {
                mPaint.color = ContextCompat.getColor(context, R.color.rect)
                canvas.drawRect(0f, 0f, mWidth * 1.0f, mWidth * 1.0f, mPaint)

            }
            SHAPE.TRIANGLE -> {
                mPaint.color = ContextCompat.getColor(context, R.color.triangle)
                val path = Path()
                val dy = sqrt(mWidth * mWidth.toDouble() - mWidth * mWidth.toDouble() / 4).toFloat()

                path.moveTo(mWidth / 2.0f, .0f)
                path.lineTo(.0f, dy)
                path.lineTo(mWidth * 1.0f, dy)
                canvas.drawPath(path, mPaint)
                path.close()
            }
        }
    }

    fun exchangeShape() {
        mCurrentShape = when (mCurrentShape) {
            SHAPE.CIRCLE -> {
                SHAPE.RECT
            }
            SHAPE.RECT -> {
                SHAPE.TRIANGLE
            }
            SHAPE.TRIANGLE -> {
                SHAPE.CIRCLE
            }
        }
        invalidate()
    }

    enum class SHAPE {
        TRIANGLE, CIRCLE, RECT
    }



}