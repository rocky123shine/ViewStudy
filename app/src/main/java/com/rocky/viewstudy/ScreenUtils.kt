package com.rocky.viewstudy

import android.content.res.Resources
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.res.ResourcesCompat

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/21
 * </pre>
 */
private val displayMetrics = Resources.getSystem().displayMetrics

val Int.dp: Int get() = (this * displayMetrics.density).toInt()
val Float.dp: Int get() = (this * displayMetrics.density).toInt()
val Int.sp: Int get() = (this * displayMetrics.scaledDensity).toInt()
val Float.sp: Int get() = (this * displayMetrics.scaledDensity).toInt()
fun Int.px2dp(): Int = (this / displayMetrics.density).toInt()
fun Int.px2sp(): Int = (this / displayMetrics.scaledDensity).toInt()

fun color( colorId: Int) = ResourcesCompat.getColor(Resources.getSystem(),colorId,null)

