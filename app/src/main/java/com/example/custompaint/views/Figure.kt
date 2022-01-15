package com.example.custompaint.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import com.example.custompaint.R
import kotlinx.coroutines.*
import kotlin.math.*

class Figure(context: Context, width: Int, height: Int) {

    private val spot = ContextCompat.getDrawable(context, R.drawable.point)!!
    private var currentTime = System.currentTimeMillis()
    private val modifiedPoints: List<Point> by lazy { getPolarPoints() }
    private val animator = ValueAnimator.ofInt(0, 100).apply {
        duration = 10000
        addListener(onEnd = { isFinished = true })
        addUpdateListener(::animateXAxis)
        addUpdateListener(::animateYAxis)
    }
    private val scope = CoroutineScope(Dispatchers.Main).launch {
        while (System.currentTimeMillis() - currentTime < 3000) delay(1000)
        launch(Dispatchers.Main) { animator.start() }
    }

    val bitmap: Bitmap = createBitmap(width, height)
    var points = mutableListOf<Point>()
        set(value) {
            if (animator.isStarted) return
            bitmap.applyCanvas {
                value.forEach {
                    spot.setBounds(it.x - 5, it.y - 5, it.x + 5, it.y + 5)
                    spot.draw(this)
                }
            }
            currentTime = System.currentTimeMillis()
            field += value
        }
    var isNotChecked = true
    var isFinished = false
        private set
    val isNotFinished
        get() = !isFinished

    //    это функция r(theta)
    private val r =
        { theta: Double -> sin(theta).pow(3) * cos(2 * theta).pow(2) + cos(theta).pow(2) / 2 }

    private val barycenter: (List<Point>) -> Point = {
        it.reduce { acc, point -> Point(acc.x + point.x, acc.y + point.y) }
            .apply { x /= it.size; y /= it.size }
    }

    private fun thetas(from: Double, to: Double, scale: Int): List<Double> = if (from < to) {
        val dx = r(from) * scale * cos(from) - r(from + PI / 180) * scale * cos(from + PI / 180)
        val dy = r(from) * scale * sin(from) - r(from + PI / 180) * scale * sin(from + PI / 180)
        val dl = sqrt(dx * dx + dy * dy)
        val dTheta = 5 / dl * PI / 180
        thetas(from + dTheta, to, scale) + from
    } else {
        listOf(to)
    }

    private fun getPolarPoints(): List<Point> {
        val center = barycenter(points)
        val scale = 500 * points.size / 340
        val thetaFrom = -PI / 6
        val thetaTo = 7 * PI / 6
        return thetas(thetaFrom, thetaTo, scale).map { theta ->
            Point(
                (r(theta) * scale * cos(theta)).toInt() + center.x,
                (r(theta) * scale * sin(theta)).toInt() + center.y
            )
        }
    }

    private fun animateXAxis(animator: ValueAnimator) {
        if (System.currentTimeMillis() - currentTime < 3000) return
        while (points.size < modifiedPoints.size) points.add(points.last())
        while (points.size > modifiedPoints.size) points.remove(points.last())
        for (i in 0 until points.size) {
            val dx = modifiedPoints[i].x - points[i].x
            val dy = modifiedPoints[i].y - points[i].y
            points[i].x += dx * animator.animatedValue as Int / 100
            points[i].y += dy * animator.animatedValue as Int / 100
        }
        bitmap.applyCanvas {
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            points.forEach {
                spot.setBounds(it.x - 5, it.y - 5, it.x + 5, it.y + 5)
                spot.draw(this)
            }
        }
    }

    private fun animateYAxis(animator: ValueAnimator) {
        if (System.currentTimeMillis() - currentTime < 3000) return
    }
}