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

class Figure(context: Context, private val width: Int, private val height: Int)  {

    private val spot = ContextCompat.getDrawable(context, R.drawable.point)!!
    private val animator = ValueAnimator.ofInt(0, 100).apply {
        duration = 1000
        addUpdateListener(::animateXAxis)
        addUpdateListener(::animateYAxis)
        start()
    }

    var points: ArrayList<Point> = arrayListOf()
    val bitmap: Bitmap
    get() = createBitmap(width, height).applyCanvas {
        points.forEach {
            spot.setBounds(it.x - 5, it.y - 5, it.x + 5, it.y + 5)
            spot.draw(this)
        }
    }

    private val functionFuck = { theta:Double -> sin(theta).pow(3) * cos(2*theta).pow(2) + cos(theta).pow(2)/2 }

    private fun getFuckPoints(): List<Point> {
        val centerX = width/2
        val centerY = height/2
        val scale = height - centerY/2
        val step = PI /180
        val thetaFrom = -PI /6
        val thetaTo = 7* PI /6
        val range = ((thetaTo-thetaFrom)/step).toInt()
        val thetas = (0..range).map { it*step + thetaFrom }
        return thetas.map {
            Point(
                (functionFuck(it) * scale * cos(it)).toInt() + centerX,
                (functionFuck(it) * scale * sin(it)).toInt() + centerY/2
            )
        }
    }

    private fun animateXAxis(animator: ValueAnimator) {

    }

    private fun animateYAxis(animator: ValueAnimator) {

    }
}