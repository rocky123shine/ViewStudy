package com.rocky.viewstudy.anim.anim02.v2.linstenner

import android.graphics.PointF

interface MessageDragBubbleViewListener {
    fun restore()
    fun onDismiss(pointF: PointF)
}