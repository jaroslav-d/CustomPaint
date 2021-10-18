package com.example.custompaint.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import com.example.custompaint.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Figure(context: Context, width: Int, height: Int)  {

    private val spot = ContextCompat.getDrawable(context, R.drawable.point)!!
    private val animator = ValueAnimator.ofInt(0, 100).apply {
        duration = 1000
        addUpdateListener(::animateXAxis)
        addUpdateListener(::animateYAxis)
        start()
    }

    val bitmap: Bitmap = createBitmap(width, height)
    var points = mutableListOf<Point>()
    set(value) {
        bitmap.applyCanvas {
            value.forEach {
                spot.setBounds(it.x - 5, it.y - 5, it.x + 5, it.y + 5)
                spot.draw(this)
            }
        }
        field += value
    }

//    это функция r(theta)
    private val r = { theta:Double -> sin(theta).pow(3) * cos(2*theta).pow(2) + cos(theta).pow(2)/2 }
//    это производная r'(theta) от функции r(theta)
    private val r_ = { theta:Double -> sin(theta) * (3*sin(theta) * cos(theta) * cos(2*theta).pow(2) - cos(theta) - 4*sin(theta).pow(2) * sin(2*theta) * cos(2*theta)) }

    private val barycenter: (List<Point>) -> Point = {
        it.reduce { acc, point -> acc.apply { x += point.x; y += point.y } }
            .apply { x /= it.size; y /= it.size }
    }

    private fun getPolarPoints(): List<Point> {
        val center = barycenter(points)
        val scale = 500 // maybe dp or dpi or ???
        val step = PI /180 /scale*100
        val thetaFrom = -PI /6
        val thetaTo = 7* PI /6
//        val thetas = listOf<Double>().apply    {
//            var thetaCurrent = thetaFrom
//            while (thetaCurrent < thetaTo) {
//
//            }
//        }
        val range = ((thetaTo-thetaFrom)/step).toInt()
        val thetas = (0..range).map { it*step + thetaFrom }
        val crudePoints = thetas.map { theta ->
            Point(
                (r(theta) * scale * cos(theta)).toInt() + center.x,
                (r(theta) * scale * sin(theta)).toInt() + center.y
            )
        }
        return crudePoints
    }

    private fun animateXAxis(animator: ValueAnimator) {

    }

    private fun animateYAxis(animator: ValueAnimator) {

    }
}