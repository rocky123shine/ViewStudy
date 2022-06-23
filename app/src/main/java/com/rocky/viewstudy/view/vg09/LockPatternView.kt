package com.rocky.viewstudy.view.vg09

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.*

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/14
 * </pre>
 */
public class LockPatternView : View {
    private var isInit = false;
    private var mDotRadius = 0
    private val mPoints: Array<Array<Point?>> = Array(3) { Array(3) { null } }
    private lateinit var mLinepaint: Paint
    private lateinit var mPressedPaint: Paint
    private lateinit var mErrorPaint: Paint
    private lateinit var mNormalPaint: Paint
    private lateinit var mArrowPaint: Paint

    private val mOuterPressedColor = 0xff8cbad8.toInt()
    private val mInnerPressedColor = 0xff0596f6.toInt()
    private val mOuterNormalColor = 0xffd9d9d9.toInt()
    private val mInnerNormalColor = 0xff929292.toInt()
    private val mOuterErrorColor = 0xff901032.toInt()
    private val mInnerErrorColor = 0xffea0945.toInt()


    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context,
        attributeSet,
        defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        //  super.onDraw(canvas)

        if (!isInit) {
            //初始化点 和 画笔
            initDot()
            initPaint()
        }
        isInit = true

        //绘制九宫格
        drawShow(canvas)
    }

    private var mMovingX = 0f
    private var mMovingY = 0f
    private var isTouch = false
    private var list: ArrayList<Point> = ArrayList()
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        mMovingX = event?.x ?: 0f
        mMovingY = event?.y ?: 0f
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (null != point) {
                    //此时为按下
                    point!!.status = point!!.STATUS_PRESSED
                    isTouch = true
                    list.add(point!!)
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isTouch) {
                    return true
                }
                mMovingX = event.x
                mMovingY = event.y

                if (null != point) {
                    //此时为按下
                    point!!.status = point!!.STATUS_PRESSED
                    if (!list.contains(point!!)) {
                        list.add(point!!)
                    }
                    invalidate()
                }

            }
            MotionEvent.ACTION_UP -> {

                cancelStatus()
            }

        }
        return true
    }

    private fun drawShow(canvas: Canvas?) {

        for (i in 0..2) {
            //第i行格子
            for (j in 0..2) {
                val point = mPoints[i][j] ?: return
                //根据状态 选择画笔
                when (point.status) {
                    point.STATUS_NORMAL -> {
                        //外圆
                        mNormalPaint.color = mOuterNormalColor
                        canvas?.drawCircle(point.currentX.toFloat(),
                            point.currentY.toFloat(),
                            mDotRadius.toFloat(),
                            mNormalPaint)
                        //内圆
                        mNormalPaint.color = mInnerNormalColor
                        canvas?.drawCircle(point.currentX.toFloat(),
                            point.currentY.toFloat(),
                            mDotRadius / 6.toFloat(),
                            mNormalPaint)
                    }
                    point.STATUS_PRESSED -> {
                        //外圆
                        mPressedPaint.color = mOuterPressedColor
                        canvas?.drawCircle(point.currentX.toFloat(),
                            point.currentY.toFloat(),
                            mDotRadius.toFloat(),
                            mNormalPaint)
                        //内圆
                        mPressedPaint.color = mInnerPressedColor
                        canvas?.drawCircle(point.currentX.toFloat(),
                            point.currentY.toFloat(),
                            mDotRadius / 6.toFloat(),
                            mNormalPaint)
                    }
                    point.STATUS_ERROR -> {
                        //外圆
                        mErrorPaint.color = mOuterErrorColor
                        canvas?.drawCircle(point.currentX.toFloat(),
                            point.currentY.toFloat(),
                            mDotRadius.toFloat(),
                            mNormalPaint)
                        //内圆
                        mErrorPaint.color = mInnerErrorColor
                        canvas?.drawCircle(point.currentX.toFloat(),
                            point.currentY.toFloat(),
                            mDotRadius / 6.toFloat(),
                            mNormalPaint)
                    }
                }

            }
        }

        //绘制线
        drawLine(canvas)

    }

    private fun drawLine(canvas: Canvas?) {
        if (list.size >= 1) {
            var lastPoint = list[0]

            for (index in 1..list.size - 1) {
                drawLine(lastPoint,
                    list[index].currentX,
                    list[index].currentY,
                    canvas!!,
                    mLinepaint)
                drawArrow(canvas, mArrowPaint, lastPoint, list[index], mDotRadius / 3f, 38)
                lastPoint = list[index]

            }
            drawLine(lastPoint, mMovingX.toInt(), mMovingY.toInt(), canvas!!, mLinepaint)

        }

    }

    private fun drawArrow(
        canvas: Canvas,
        paint: Paint,
        start: Point,
        end: Point,
        arrowHeight: Float,
        angle: Int,
    ) {
        val d = getDistance(start, end)
        val sin_B = ((end.currentX - start.currentX) / d).toFloat()
        val cos_B = ((end.currentY - start.currentY) / d).toFloat()
        val tan_A = Math.tan(Math.toRadians(angle.toDouble())).toFloat()
        val h = (d - arrowHeight.toDouble() - mDotRadius * 1.1).toFloat()
        val l = arrowHeight * tan_A
        val a = l * sin_B
        val b = l * cos_B
        val x0 = h * sin_B
        val y0 = h * cos_B
        val x1 = start.currentX + (h + arrowHeight) * sin_B
        val y1 = start.currentY + (h + arrowHeight) * cos_B
        val x2 = start.currentX + x0 - b
        val y2 = start.currentY.toFloat() + y0 + a
        val x3 = start.currentX.toFloat() + x0 + b
        val y3 = start.currentY + y0 - a
        val path = Path()
        path.moveTo(x1, y1)
        path.lineTo(x2, y2)
        path.lineTo(x3, y3)
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawLine(start: Point, endX: Int, endY: Int, canvas: Canvas, paint: Paint) {
        val d = getDistance(start, Point(endX, endY, 0))
        val dx = endX - start.currentX
        val dy = endY - start.currentY

        val rx = (dx / d * (mDotRadius / 6.0)).toFloat()
        val ry = (dy / d * (mDotRadius / 6.0)).toFloat()

        canvas.drawLine(
            start.currentX + rx,
            start.currentY + ry,
            endX - rx,
            endY - ry, paint)
    }

    private fun initPaint() {

        mLinepaint = Paint()
        mLinepaint.color = mInnerPressedColor
        mLinepaint.style = Paint.Style.STROKE
        mLinepaint.isAntiAlias = true
        mLinepaint.strokeWidth = mDotRadius / 6f.toFloat()
// 按下的画笔
        mPressedPaint = Paint()
        mPressedPaint.style = Paint.Style.STROKE
        mPressedPaint.isAntiAlias = true
        mPressedPaint.strokeWidth = (mDotRadius / 6).toFloat()
//错误的画笔
        mErrorPaint = Paint()
        mErrorPaint.style = Paint.Style.STROKE
        mErrorPaint.isAntiAlias = true
        mErrorPaint.strokeWidth = (mDotRadius / 6).toFloat()
//默认的画笔
        mNormalPaint = Paint()
        mNormalPaint.style = Paint.Style.STROKE
        mNormalPaint.isAntiAlias = true
        mNormalPaint.strokeWidth = (mDotRadius / 6).toFloat()
// 箭头的画笔
        mArrowPaint = Paint()
        mArrowPaint.color = mInnerPressedColor
        mArrowPaint.style = Paint.Style.FILL
        mArrowPaint.isAntiAlias = true

    }

    private fun initDot() {
        //计算 宽高
        var height1 = height
        var width1 = width
        height1 = height - paddingTop - paddingBottom
        width1 = width - paddingEnd - paddingStart

        val currentWidth = Math.min(height1, width1)
        val top = (height - currentWidth) / 2
        val left = (width - currentWidth) / 2
        //绘制九宫格  就是绘制3*3的格子
        val perWidth = currentWidth / 3
        Log.d("TAG", "initDot: left $left  , top  $top")

        Log.d("TAG", "initDot: perWidth $perWidth")
        mDotRadius = perWidth / 4
        for (i in 0..2) {
            //第i行格子
            for (j in 0..2) {
                //mPoints[i][j] = Point(i, j, 3 * i + (j + 1))
                //Log.d("TAG", "initDot: index"+(3 * i + (j + 1)))
                mPoints[i][j] = Point(left + j * perWidth + perWidth / 2,
                    i * perWidth + perWidth / 2 + top,
                    3 * i + (j + 1))

                Log.d("TAG", "mPoints[$i][$j] -->" + mPoints[i][j])
            }
        }
    }

    private val point: Point?
        get() {
            for (mPoint in mPoints) {
                for (point in mPoint) {
                    if (checkPointInBounds(
                            point!!.currentX.toFloat(),
                            point.currentY.toFloat(),
                            mDotRadius.toFloat(),
                            mMovingX, mMovingY
                        )
                    ) {
                        return point
                    }
                }
            }
            return null
        }

    private fun cancelStatus() {
        list.clear()
        for (mPoint in mPoints) {
            for (point in mPoint) {
                point!!.status = point!!.STATUS_NORMAL
            }
        }
        invalidate()
    }

    private fun checkPointInBounds(
        x: Float,
        y: Float,
        r: Float,
        targetX: Float,
        targetY: Float,
    ): Boolean {
        return Math.sqrt((x.toDouble() - targetX.toDouble()) * (x.toDouble() - targetX.toDouble()) +
                (y.toDouble() - targetY.toDouble()) * (y.toDouble() - targetY.toDouble())) < r
    }

    class Point(val currentX: Int, val currentY: Int, var index: Int) {
        val STATUS_NORMAL = 1
        val STATUS_PRESSED = 2
        val STATUS_ERROR = 3
        var status = STATUS_NORMAL

        override fun toString(): String {
            return " currentX-> $currentX currentY->$currentY index->$index"
        }

    }


    private fun getDistance(start: Point, end: Point): Double {
        return Math.sqrt(
            (start.currentX - end.currentX).toDouble() * (start.currentX - end.currentX)
                    + (start.currentY - end.currentY).toDouble() * (start.currentY - end.currentY)
        );
    }
}