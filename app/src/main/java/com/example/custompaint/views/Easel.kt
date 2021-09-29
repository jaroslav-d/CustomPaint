package com.example.custompaint.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import androidx.core.graphics.createBitmap
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.applyCanvas
import kotlin.math.*


class Easel(context: Context, attr: AttributeSet) : View(context, attr) {

    private var fillpoints = mutableListOf<Point>()
    private val figures = mutableListOf<Figure>()
    private val currentState: Bitmap by lazy { createBitmap(width, height) }
    private var animator = ValueAnimator.ofInt(0,100).apply {
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        duration = 1000
        addUpdateListener {
            if (figures.isEmpty()) return@addUpdateListener
            currentState.applyCanvas {
                drawBitmap(figures.last().bitmap, 0f, 0f, null)
            }
            invalidate()
        }
        start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            figures.add(Figure(context, width, height))
            fillpoints.clear()
            fillpoints.add(Point(event.x.toInt(), event.y.toInt()))
            return true
        }
        val lastPoint = fillpoints.last()
        val deltaX = event.x.toInt() - lastPoint.x
        val deltaY = event.y.toInt() - lastPoint.y
        val distance = sqrt((deltaX*deltaX + deltaY*deltaY).toFloat())
        val numberPoints = (distance/5).toInt() + 1
        fillpoints = (1..numberPoints).map {
            Point(event.x.toInt() - (deltaX - deltaX/numberPoints*it),
                event.y.toInt() - (deltaY - deltaY/numberPoints*it))
        } as MutableList<Point>
        figures.last().points = fillpoints
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        canvas.drawBitmap(currentState, 0f, 0f, null)
    }

    private fun rebuildCurrentState() {
        if (figures.isEmpty()) return
        currentState.applyCanvas {
            this.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            figures.forEach { drawBitmap(it.bitmap, 0f, 0f, null) }
        }
    }

}