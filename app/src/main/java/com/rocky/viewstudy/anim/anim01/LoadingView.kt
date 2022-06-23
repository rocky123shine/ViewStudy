package com.rocky.viewstudy.anim.anim01

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.animation.addListener
import com.rocky.viewstudy.R

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/17
 * </pre>
 */
@RequiresApi(Build.VERSION_CODES.O)
class LoadingView : LinearLayout {
    private var shapeView: ShapeView
    private var shadowView: View
    private var mTranslationDistance = 0.0f
    private val ANIMATOR_DURATION: Long = 500
    private var isAnimStop: Boolean = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr)

    init {
        orientation = VERTICAL
        inflate(context, R.layout.anim_01_comb, this)
        shapeView = findViewById(R.id.shapeView)
        shadowView = findViewById(R.id.vBg)
        mTranslationDistance = dp2px(82.0f)
        //resume 执行之后 在执行动画
        post {
            startFallAnim()
        }

    }

    private fun startFallAnim() {
        if (isAnimStop) {
            return
        }

        val animator = ObjectAnimator.ofFloat(shapeView, "translationY", .0f, mTranslationDistance)
        animator.duration = ANIMATOR_DURATION
        //animator.start()
        val scaleAnimator = ObjectAnimator.ofFloat(shadowView, "scaleX", 1f, 0.3f)
        scaleAnimator.duration = ANIMATOR_DURATION
        //  scaleAnimator.start()

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animator, scaleAnimator)

        animatorSet.interpolator = AccelerateInterpolator()


        animatorSet.addListener(
            onStart = {
                startRotation()
            },
            onEnd = {
                starUpAnim()
            }
        )

        animatorSet.start()
    }

    private fun startRotation() {
        when (shapeView.mCurrentShape) {
            ShapeView.SHAPE.RECT -> {
                ObjectAnimator.ofFloat(shapeView, "rotation", 0.0f, 180.0f)
                    .setDuration(ANIMATOR_DURATION).start()
            }
            ShapeView.SHAPE.TRIANGLE -> {
                ObjectAnimator.ofFloat(shapeView, "rotation", 0.0f, -120.0f)
                    .setDuration(ANIMATOR_DURATION).start()
            }
            ShapeView.SHAPE.CIRCLE -> {}
        }

    }

    private fun starUpAnim() {
        if (isAnimStop) {
            return
        }
        val animator = ObjectAnimator.ofFloat(shapeView, "translationY", mTranslationDistance, .0f)
        animator.duration = ANIMATOR_DURATION
        //animator.start()
        val scaleAnimator = ObjectAnimator.ofFloat(shadowView, "scaleX", 0.3f, 1.0f)
        scaleAnimator.duration = ANIMATOR_DURATION
        //  scaleAnimator.start()

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animator, scaleAnimator)

        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.start()
        animatorSet.addListener(
            onEnd = {
                shapeView.exchangeShape()
                startFallAnim()

            }
        )


    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(View.INVISIBLE)
//        shapeView.clearAnimation()
//        shadowView.clearAnimation()
//        val p = parent as? ViewGroup
//        if (null != p) {
//            p.removeView(this)
//            removeAllViews()
//        }
//        isAnimStop = true
    }

    fun dp2px(dp: Float): Float {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

}