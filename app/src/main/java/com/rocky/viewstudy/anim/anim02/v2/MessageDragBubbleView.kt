package com.rocky.viewstudy.anim.anim02.v2

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.core.animation.addListener
import com.rocky.viewstudy.anim.anim02.v2.linstenner.DragViewDisappearListener
import com.rocky.viewstudy.anim.anim02.v2.linstenner.MessageDragBubbleViewListener
import com.rocky.viewstudy.anim.anim02.v2.linstenner.MessageDragViewTouchListener

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/20
 * </pre>
 */
class MessageDragBubbleView : View {
    private var mBubblePaint: Paint = Paint()
    private var mBubbleColor: Int = Color.RED


    //两个圆的半径
    private var mMinRadius = 4
    private var mFixedRadius = 12
    private var mMovingRadius = 16
    private var mCurrentFixedRadius = mFixedRadius
    var canDraw = false
    private var mBitmap: Bitmap? = null
    var mListener: MessageDragBubbleViewListener? = null

    companion object {
        // 1. targetView 怎么关联 MessageDragBubble??

        //两个点  一个固定点 一个跟随手指移动
        val mFixedPoint: PointF = PointF(.0f, .0f)
        val mMovePoint: PointF = PointF(.0f, .0f)

        @JvmStatic
        fun attach(view: View?, listener: DragViewDisappearListener) {
            if (null == view) {
                throw IllegalArgumentException("targetView==null  need a targetView！！！")
            }
            view.setOnTouchListener(MessageDragViewTouchListener(view, listener))
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context,
        attributeSet,
        defStyleAttr)

    init {
        mBubblePaint.color = mBubbleColor
        mBubblePaint.isAntiAlias = true
    }


//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        event ?: return false
//        when (event.actionMasked) {
//            MotionEvent.ACTION_DOWN -> {
//                //更新固定点位置
//                mFixedPoint.x = event.rawX
//                mFixedPoint.y = event.rawY
//
//            }
//            MotionEvent.ACTION_MOVE -> {
//                canDraw = true
//                //更新移动点位置
//                mMovePoint.x = event.rawX
//                mMovePoint.y = event.rawY
//            }
//            MotionEvent.ACTION_UP -> {
//                canDraw = false
//            }
//            else -> {
//                canDraw = false
//            }
//        }
//        //绘制 两个圆
//        invalidate()
//        return true
//    }


    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        val distance = getDistance(mFixedPoint, mMovePoint)
        val fixR = dp2px(mFixedRadius * 1f) - distance / 14
        mCurrentFixedRadius = fixR.toInt()
        if (dp2px(mMinRadius * 1f) < fixR) {
            //大于最新半径 才绘制 固定圆
            canvas.drawCircle(mFixedPoint.x, mFixedPoint.y, fixR, mBubblePaint)
            //绘制贝塞尔曲线
            val path = getPath(fixR)
            canvas.drawPath(path, mBubblePaint)
        }
        //canvas.drawCircle(mMovePoint.x, mMovePoint.y, dp2px(mMovingRadius * 1f), mBubblePaint)
        mBitmap?.let {
            canvas.drawBitmap(it,
                mMovePoint.x - it.width / 2,
                mMovePoint.y - it.height / 2,
                null)
        }


    }

    private fun getPath(currentFixR: Float): Path {
        val path = Path()
        path.reset()

        //记录 两个圆的圆心分别为O 和 P
        val o_x = mFixedPoint.x
        val o_y = mFixedPoint.y
        val p_x = mMovePoint.x
        val p_y = mMovePoint.y
        //path move 到 A 点
        // 其中 A 点 D 点 关于 O点对称 AD 垂直于OP  B 和 C 关于P 对称 BC 垂直 OP
        //使用 三角函数 可以得到以下 关系
        val dx = p_x - o_x
        val dy = p_y - o_y
        var arcTanA = Math.atan(dy.toDouble() / dx)
        //A 点 可以用 arcTanA 角度 和 O  表示
        // (A.x - o.x) = currentFixR * sin(arcTanA)
        // (A.y - o.y) = currentFixR * cos(arcTanA)
        val a_x = currentFixR * Math.sin(arcTanA) + o_x
        val a_y = currentFixR * Math.cos(arcTanA) + o_y
        //A D 关于O 对称 则 D 可以得到坐标  a_x + d_x = 2 * o_x  a_y + d_y = 2 * o_y
        val d_x = 2 * o_x - a_x
        val d_y = 2 * o_y - a_y

        // B点
        // (B.x - p.x) = dp2px(mMovingRadius) * sin(arcTanA)
        // (B.y - p.y) = dp2px(mMovingRadius) * cos(arcTanA)

        val b_x = dp2px(mMovingRadius * 1f) * Math.sin(arcTanA) + p_x
        val b_y = dp2px(mMovingRadius * 1f) * Math.cos(arcTanA) + p_y
        //B C 关于P 对称 则 C 可以得到坐标  b_x + c_x = 2 * p_x  b_y + c_y = 2 * p_y

        val c_x = 2 * p_x - b_x
        val c_y = 2 * p_y - b_y

        path.moveTo(a_x.toFloat(), a_y.toFloat())
        //我们设控制点为 distance 的黄金分割 C
        val ctr_x = mFixedPoint.x + (mMovePoint.x - mFixedPoint.x) * .382
        val ctr_y = mFixedPoint.y + (mMovePoint.y - mFixedPoint.y) * .382

        path.quadTo(ctr_x.toFloat(), ctr_y.toFloat(), b_x.toFloat(), b_y.toFloat())
        path.lineTo(c_x.toFloat(), c_y.toFloat())
        path.quadTo(ctr_x.toFloat(), ctr_y.toFloat(), d_x.toFloat(), d_y.toFloat())


//
//        Log.d("TAG", "getPath: " +
//                "point1  ${mFixedPoint.toString()}\n" +
//                "point2  ${mMovePoint.toString()}\n " +
//                "CTR: $ctr_x , $ctr_y \n" +
//                "A: $a_x , $a_y \n" +
//                "B: $b_x , $b_y \n" +
//                "C: $b_x , $c_y \n" +
//                "D: $b_x , $d_y \n")
        path.close()
        return path
    }

    private fun getDistance(start: PointF, end: PointF): Float {
        return Math.sqrt(
            (start.x - end.x).toDouble() * (start.x - end.x) +
                    (start.y - end.y).toDouble() * (start.y - end.y)
        ).toFloat()
    }


    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }


    fun initPoint(rawX: Float, rawY: Float, bitmap: Bitmap) {
        mFixedPoint.x = rawX
        mFixedPoint.y = rawY
        mBitmap = bitmap
        updatePoint(rawX, rawY)
    }

    fun updatePoint(rawX: Float, rawY: Float) {
        mMovePoint.x = rawX
        mMovePoint.y = rawY
        invalidate()
    }

    fun handleUpEvent() {
        if (mCurrentFixedRadius < dp2px(mMinRadius * 1f)) {
            mListener?.onDismiss(mMovePoint)
        } else {
            val valueAnimator = ObjectAnimator.ofFloat(1f, 0f)
            valueAnimator.duration = 350
            valueAnimator.addUpdateListener {
                val percent = it.animatedValue as Float
                val movingPoint = getMovingPoint(percent)
                updatePoint(movingPoint.x, movingPoint.y)
            }

            valueAnimator.interpolator = BounceInterpolator()
            valueAnimator.addListener(
                onEnd = {
                    mListener?.restore()
                }
            )
            valueAnimator.start()
        }
    }

    private fun getMovingPoint(percent: Float): PointF {
        return PointF(mFixedPoint.x + (mMovePoint.x - mFixedPoint.x) * percent,
            mFixedPoint.y + (mMovePoint.y - mFixedPoint.y) * percent)
    }

    fun getStatusBarHeight(): Int {
        val identifier = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (identifier > 0) resources.getDimensionPixelOffset(identifier) else dp2px(25f).toInt()
    }
}