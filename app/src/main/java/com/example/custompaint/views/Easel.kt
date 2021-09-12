package com.example.custompaint.views

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import android.graphics.Canvas
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.applyCanvas
import com.example.custompaint.R
import kotlin.math.*


class Easel(context: Context, attr: AttributeSet) : View(context, attr) {

    private var fillpoints = mutableListOf<Point>()
    private val spot = ContextCompat.getDrawable(context, R.drawable.point)!!
    private val figures = mutableListOf<Figure>()
    private var currentState: Bitmap? = null

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
        } as ArrayList<Point>
        figures.last().points += fillpoints
        if (event.actionMasked == MotionEvent.ACTION_UP) {
            currentState = null
        }
        currentState = loadBitmapFromView()
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        if (currentState != null) canvas.drawBitmap(currentState!!, 0f, 0f, null)
    }

    private fun loadBitmapFromView(): Bitmap {
        return createBitmap(width, height).applyCanvas {
            if (currentState != null) drawBitmap(currentState!!, 0f, 0f, null)
            fillpoints.forEach {
                spot.setBounds(it.x - 5, it.y - 5, it.x + 5, it.y + 5)
                spot.draw(this)
            }
        }
    }

}