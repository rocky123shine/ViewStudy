package com.rocky.viewstudy.anim.anim04

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.rocky.viewstudy.R
import com.rocky.viewstudy.dp
import kotlin.math.min

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/22
 * </pre>
 */
class TencentClassLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val mPaint: Paint = Paint()
    private val minWidth = 120.dp
    private var tencentBitmap: Bitmap? = null
    private var mWidth = 0
    private var mHeight = 0
    private var mLeft = 0f
    private var mTop = 0f
    private var mRight = 0f
    private var mBottom = 0f
    private var currentWidthProgress = 0f

    init {
        mPaint.isDither = true
        mPaint.isAntiAlias = true
        mPaint.color = Color.parseColor("#E600A2E8")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = if (widthMode != MeasureSpec.EXACTLY) {
            minWidth
        } else width
        mHeight = if (heightMode != MeasureSpec.EXACTLY) {
            minWidth
        } else height

        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        tencentBitmap?.run {
            canvas.drawBitmap(this,
                mLeft,
                mTop,
                mPaint)

            val saveLayerId = canvas.saveLayer(mLeft, mTop, mRight, mBottom, mPaint)
            canvas.drawBitmap(this,
                mLeft,
                mTop,
                mPaint)
            mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            //绘制水波纹
            drawPath(canvas)
            mPaint.xfermode = null
            canvas.restoreToCount(saveLayerId)

        }

    }


    private fun drawPath(canvas: Canvas) {
        val path: Path = getPath()
        canvas.drawPath(path, mPaint)
    }

    private fun getPath(): Path {
        val path = Path()
        path.moveTo(mLeft + currentWidthProgress - tencentBitmap!!.width,
            mTop + tencentBitmap!!.height / 2f)
        path.quadTo(
            mLeft + currentWidthProgress + tencentBitmap!!.width / 8f - tencentBitmap!!.width,
            mTop + tencentBitmap!!.height * 5 / 8f,

            mLeft + currentWidthProgress + tencentBitmap!!.width / 4f - tencentBitmap!!.width,
            mTop + tencentBitmap!!.height * 1 / 2f)

        path.quadTo(mLeft + currentWidthProgress + tencentBitmap!!.width * 3 / 8f - tencentBitmap!!.width,
            mTop + tencentBitmap!!.height * 3 / 8f,

            mLeft + currentWidthProgress + tencentBitmap!!.width * 1f / 2 - tencentBitmap!!.width,
            mTop + tencentBitmap!!.height * 1 / 2f)

        path.quadTo(mLeft + currentWidthProgress + tencentBitmap!!.width * 5 / 8f - tencentBitmap!!.width,
            mTop + tencentBitmap!!.height * 5 / 8f,

            mLeft + currentWidthProgress + tencentBitmap!!.width * 3f / 4 - tencentBitmap!!.width,
            mTop + tencentBitmap!!.height * 1 / 2f)

        path.quadTo(mLeft + currentWidthProgress + tencentBitmap!!.width * 7 / 8f - tencentBitmap!!.width,
            mTop + tencentBitmap!!.height * 3 / 8f,

            mLeft + currentWidthProgress + tencentBitmap!!.width * 1f - tencentBitmap!!.width,
            mTop + tencentBitmap!!.height * 1 / 2f)

        path.lineTo(mLeft + tencentBitmap!!.width * 1f, mTop + tencentBitmap!!.height * 1f)
        path.lineTo(mLeft, mTop + tencentBitmap!!.height * 1f)
        path.close()
        return path
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        tencentBitmap = getImage(R.drawable.tencent_iv, min(w, h))

        mLeft = (mWidth - tencentBitmap!!.width) / 2f
        mTop = (mHeight - tencentBitmap!!.height) / 2f
        mRight = (mWidth - tencentBitmap!!.width) / 2f + tencentBitmap!!.width
        mBottom = (mHeight - tencentBitmap!!.height) / 2f + tencentBitmap!!.height

        executeAnim()

    }

    private fun executeAnim() {
        val widthAnimator = ObjectAnimator.ofFloat(0f, tencentBitmap!!.width.toFloat())
        widthAnimator.addUpdateListener {
            currentWidthProgress = it.animatedValue as Float
            invalidate()
        }
        widthAnimator.duration = 1000
        widthAnimator.repeatCount = -1
        widthAnimator.start()
    }


    private fun getImage(drawable: Int, requestSize: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        //拿到 drawable 的信息
        BitmapFactory.decodeResource(resources, drawable, options)
        options.inTargetDensity = requestSize
        options.inDensity = options.outWidth
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resources, drawable, options)

    }

}