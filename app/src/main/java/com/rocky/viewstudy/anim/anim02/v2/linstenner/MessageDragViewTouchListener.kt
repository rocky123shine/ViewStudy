package com.rocky.viewstudy.anim.anim02.v2.linstenner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.PointF
import android.graphics.drawable.AnimationDrawable
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import com.rocky.viewstudy.R
import com.rocky.viewstudy.anim.anim02.v2.MessageDragBubbleView

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/20
 * </pre>
 */
class MessageDragViewTouchListener(
    private val mTargetView: View,
    private val dismissListener: DragViewDisappearListener?,
) : View.OnTouchListener, MessageDragBubbleViewListener {

    private val mWindowManager: WindowManager =
        mTargetView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    //次数初始化MessageDragBubbleView
    private val mMessageDragBubbleView = MessageDragBubbleView(mTargetView.context)
    private val params = WindowManager.LayoutParams()

    private var mBombFrame: FrameLayout? = null
    private var mBombImage: ImageView? = null

    init {
        params.format = PixelFormat.TRANSPARENT
        mMessageDragBubbleView.mListener = this
        mBombFrame = FrameLayout(mTargetView.context)
        var layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        mBombImage = ImageView(mTargetView.context)
        mBombImage?.layoutParams = layoutParams
        mBombFrame?.addView(mBombImage)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event ?: return false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {

                mWindowManager.addView(mMessageDragBubbleView, params)
                //此时要获取 mTargetView 在屏幕中的位置
                val intArr = IntArray(2)
                mTargetView.getLocationOnScreen(intArr)
                mMessageDragBubbleView.initPoint(intArr[0].toFloat() + mTargetView.width / 2f,
                    intArr[1].toFloat() + mTargetView.height / 2f - mMessageDragBubbleView.getStatusBarHeight(),
                    getBitmap(mTargetView))
                //按下之后 把targetView 隐藏
                mTargetView.visibility = View.INVISIBLE

            }
            MotionEvent.ACTION_MOVE -> {
                mMessageDragBubbleView.canDraw = true
                mMessageDragBubbleView.updatePoint(event.rawX,
                    event.rawY - mMessageDragBubbleView.getStatusBarHeight())

            }
            MotionEvent.ACTION_UP -> {
                mMessageDragBubbleView.handleUpEvent()
            }
            else -> {
                mMessageDragBubbleView.canDraw = false
            }
        }
        return true
    }

    private fun getBitmap(mTargetView: View): Bitmap {
        val bitmap =
            Bitmap.createBitmap(mTargetView.width, mTargetView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        mTargetView.draw(canvas)
        return bitmap

    }

    override fun restore() {

        mWindowManager.removeView(mMessageDragBubbleView)
        mTargetView.visibility = View.VISIBLE

    }

    override fun onDismiss(pointF: PointF) {
        mWindowManager.removeView(mMessageDragBubbleView)
        //添加爆炸效果
        mWindowManager.addView(mBombFrame, params)
        mBombImage?.let {

            it.setBackgroundResource(R.drawable.bomb)
            val drawable = it.background as AnimationDrawable
            it.x = pointF.x - drawable.intrinsicWidth/2
            it.y = pointF.y - drawable.intrinsicHeight/2
            drawable.start()
            it.postDelayed({
                dismissListener?.dismiss()
                mWindowManager.removeView(mBombFrame)
            }, getAnimationTime(drawable))
        }

    }

    private fun getAnimationTime(drawable: AnimationDrawable): Long {
        var time = 0L
        var size = drawable.numberOfFrames
        for (i in 0 until size) {
            time += drawable.getDuration(i)

        }
        return time
    }


}