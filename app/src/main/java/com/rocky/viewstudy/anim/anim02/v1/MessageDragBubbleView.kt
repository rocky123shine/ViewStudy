package com.rocky.viewstudy.anim.anim02.v1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/20
 * </pre>
 */
class MessageDragBubbleView : View {
    private var mBubblePaint: Paint = Paint()
    private var mBubbleColor: Int = Color.RED

    //两个点  一个固定点 一个跟随手指移动
    private val mFixedPoint: PointF = PointF(.0f, .0f)
    private val mMovePoint: PointF = PointF(.0f, .0f)

    //两个圆的半径
    private var mMinRadius = 4
    private var mFixedRadius = 12
    private var mMovingRadius = 16
    private var canDraw = false


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context,
        attributeSet,
        defStyleAttr)

    init {
        mBubblePaint.color = mBubbleColor
        mBubblePaint.isAntiAlias = true
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                //更新固定点位置
                mFixedPoint.x = event.x
                mFixedPoint.y = event.y

            }
            MotionEvent.ACTION_MOVE -> {
                canDraw = true
                //更新移动点位置
                mMovePoint.x = event.x
                mMovePoint.y = event.y
            }
            MotionEvent.ACTION_UP -> {
                canDraw = false
            }
            else -> {
                canDraw = false
            }
        }
        //绘制 两个圆
        invalidate()
        return true
    }


    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        if (canDraw) {
            val distance = getDistance(mFixedPoint, mMovePoint)
            val fixR = dp2px(mFixedRadius * 1f) - distance / 14
            if (dp2px(mMinRadius * 1f) < fixR) {
                //大于最新半径 才绘制 固定圆
                canvas.drawCircle(mFixedPoint.x, mFixedPoint.y, fixR, mBubblePaint)
                //绘制贝塞尔曲线
                val path = getPath(fixR)
                canvas.drawPath(path, mBubblePaint)
            }
            canvas.drawCircle(mMovePoint.x, mMovePoint.y, dp2px(mMovingRadius * 1f), mBubblePaint)

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


}