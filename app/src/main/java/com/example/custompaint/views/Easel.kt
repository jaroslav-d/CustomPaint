package com.example.custompaint.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.custompaint.R
import kotlin.math.*


class Easel(context: Context, attr: AttributeSet) : View(context, attr) {

    private var fillpoints: ArrayList<Point> = arrayListOf()
    private val spot = ContextCompat.getDrawable(context, R.drawable.point)!!
    private var figure = Fuck(context)
    private val listeners: ArrayList<(v:View) -> Unit> = arrayListOf()
    private var preView: Bitmap? = null

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            figure = Fuck(context)
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
        figure.points += fillpoints
        if (event.actionMasked == MotionEvent.ACTION_UP) {
            listeners.forEach { it(figure) }
        }
        preView = loadBitmapFromView()
        invalidate()
        return true
    }

    fun setOnCreateShapeView(value: (v:View) -> Unit) = listeners.add(value)

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        if (preView != null) canvas.drawBitmap(preView!!, 0f, 0f, null)
        fillpoints.forEach {
            spot.setBounds(it.x - 5, it.y - 5, it.x + 5, it.y + 5)
            spot.draw(canvas)
        }
    }

    fun loadBitmapFromView(): Bitmap {
        measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val b = Bitmap.createBitmap(
            right - left,
            bottom - top,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        layout(left, top, right, bottom)
        draw(c)
        return b
    }

    private val functionFuck = { theta:Double -> sin(theta).pow(3) * cos(2*theta).pow(2) + cos(theta).pow(2)/2 }

    private fun getFuckPoints(): List<Point> {
        val centerX = (right - left)/2
        val centerY = (bottom - top)/2
        val scale = bottom - top - centerY/2
        val step = PI/180
        val thetaFrom = -PI/6
        val thetaTo = 7*PI/6
        val range = ((thetaTo-thetaFrom)/step).toInt()
        val thetas = (0..range).map { it*step + thetaFrom }
        return thetas.map {
            Point(
                (functionFuck(it) * scale * cos(it)).toInt() + centerX,
                (functionFuck(it) * scale * sin(it)).toInt() + centerY/2
            )
        }
    }

}