package com.rocky.viewstudy.test

import android.graphics.Paint
import android.util.Log

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/06/17
 *
 *     des :z 执行顺序  构造 - > 属性和init 谁在前 谁先执行
 *              最终 无论数init函数 还是 属性的声明 都会插入到构造中
 *              属性和init 函数 谁在前 谁先插入
 * </pre>
 */
open class Parent {
    // val a = println("Parent.a")
    val a = Log.d("TAG", "Parent.a")
    private val b = Log.d("TAG", "Parent.b")
    var paint: Paint

    init {
        Log.d("TAG", "Parent.init")
        paint = Paint()
    }


    constructor(arg: Int = Log.d("TAG", "Parent primary constructor default argument"))  {
        if (null == paint) {
            Log.d("TAG", "Parent.constructor call var == null ")
        }else{

            Log.d("TAG", "Parent.constructor call var != null ")
        }
    }

}

class Child : Parent {
    // val a = println("Parent.a")
    val childA = Log.d("TAG", "Child.a")

    constructor(arg: Int = Log.d("TAG", "Parent primary constructor default argument")) : super() {

    }

    init {
        Log.d("TAG", "Child.init 1")
    }

    private val b = Log.d("TAG", "Child.b")

}