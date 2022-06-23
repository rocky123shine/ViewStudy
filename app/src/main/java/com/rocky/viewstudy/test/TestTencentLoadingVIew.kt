package com.rocky.viewstudy.test

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import com.rocky.viewstudy.R
import com.rocky.viewstudy.dp
import java.lang.Math.max
import java.lang.Math.min

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/23
 *     DES    :  复习下 贝塞尔
 * </pre>
 */
class TestTencentLoadingVIew @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var tencentBitmap: Bitmap
    private val defaultSize = 150.dp

    /**
     * 波长
     */
    private var waveLength = 0f

    /**
     * 振幅
     */
    private var amplitude = 0f
    private var amplitudeLight = 0f

    //创建canvas
    private lateinit var waveBitmap: Bitmap
    private lateinit var waveCanvas: Canvas


    private val wavePath: Path = Path()
    private val WAVE_COLOR = Color.parseColor("#E600A2E8")
    private val WAVE_COLOR_LIGHT = Color.parseColor("#9900A2E8")

    var offsetX: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var offsetXLight: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    var waveHeight: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val measuredWidth = if (widthMode == MeasureSpec.EXACTLY) widthSize else defaultSize.toInt()
        val measuredHeight =
            if (heightMode == MeasureSpec.EXACTLY) heightSize else defaultSize.toInt()
        val measuredSize = min(measuredWidth, measuredHeight)
        setMeasuredDimension(measuredSize, measuredSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        tencentBitmap = getImage(R.drawable.tencent_iv, w)

        waveBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        waveCanvas = Canvas(waveBitmap)

        waveLength = w / 3f
        amplitude = h / 20f

        amplitudeLight = h / 25f
        executeAnim()
    }

    private fun executeAnim() {
        val animatorSet = AnimatorSet()
        val animator1 = ObjectAnimator.ofFloat(this, "offsetX", 0f, waveLength).apply {
            duration = 500
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART

        }
        val animator2 = ObjectAnimator.ofFloat(this, "offsetXLight", waveLength, 0f).apply {
            duration = 350
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }
        val animatorHeight = ObjectAnimator.ofFloat(this,
            "waveHeight",
            height.toFloat() + max(amplitude, amplitudeLight),
            -max(amplitude, amplitudeLight)).apply {
            duration = 2000L
            startDelay = 200L
            interpolator = AccelerateInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }
        animatorSet.playTogether(animator1, animator2, animatorHeight)
        animatorSet.start()
    }

    private fun getImage(drawable: Int, requestSize: Int): Bitmap {
        //解析 bitmap
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, drawable, options)
        options.inTargetDensity = requestSize
        options.inDensity = options.outWidth
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resources, drawable, options)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        canvas.drawBitmap(tencentBitmap, 0f, 0f, mPaint)
        drawWaveOnWaveBitmap()

        //使用离屏渲染
        val saveCount = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), mPaint)

        canvas.drawBitmap(waveBitmap,0f,0f,mPaint)
        mPaint.xfermode = xfermode
        canvas.drawBitmap(tencentBitmap,0f,0f,mPaint)
        mPaint.xfermode = null


        canvas.restoreToCount(saveCount)


    }

    private fun drawWaveOnWaveBitmap() {
        // 先清空一下 `waveBitmap` 对象的图像
        waveCanvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR)

        //第一层波纹
        mPaint.color = WAVE_COLOR_LIGHT
        updateWavePath(offsetXLight, amplitudeLight)
        waveCanvas.drawPath(wavePath, mPaint)
        //第二层波纹
        mPaint.color = WAVE_COLOR
        updateWavePath(offsetX, amplitude)
        // 这行只是使用 waveCanvas 把水波纹绘制在 waveBitmap 上而已，和屏幕没有任何关系。
        waveCanvas.drawPath(wavePath, mPaint)
    }

    private fun updateWavePath(offsetX: Float, amplitude: Float) {
        wavePath.apply {
            reset()
            moveTo(offsetX - waveLength, waveHeight)
            var waveStart = offsetX - waveLength
            while (waveStart <= width) {
                rQuadTo(waveLength / 4f, amplitude, waveLength / 2f, 0f)
                rQuadTo(waveLength / 4f, -amplitude, waveLength / 2f, 0f)
                waveStart += waveLength
            }
            lineTo(width.toFloat(), height.toFloat())
            lineTo(0f, height.toFloat())
            close()
        }
    }

}