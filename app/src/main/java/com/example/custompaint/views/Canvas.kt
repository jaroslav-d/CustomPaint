package com.example.custompaint.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.custompaint.R
import kotlin.math.*


class Canvas(context: Context, private val attr: AttributeSet) : View(context, attr) {

    private var points: ArrayList<Point> = arrayListOf()
    private val brush = ContextCompat.getDrawable(context, R.drawable.point)!!
    private var shape = Fuck(context, attr)
    private val listeners: ArrayList<(v:View) -> Unit> = arrayListOf()
    private var preView: Bitmap? = null

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            shape = Fuck(context, attr)
            points.clear()
            points.add(Point(event.x.toInt(), event.y.toInt()))
            return true
        }
        val lastPoint = points.last()
        val deltaX = event.x.toInt() - lastPoint.x
        val deltaY = event.y.toInt() - lastPoint.y
        val distance = sqrt((deltaX*deltaX + deltaY*deltaY).toFloat())
        val numberPoints = (distance/5).toInt() + 1
        Log.i("Canvas", "next number points")
        Log.i("Canvas", "${numberPoints}")
        points = (1..numberPoints).map {
            Point(event.x.toInt() - (deltaX - deltaX/numberPoints*it),
                event.y.toInt() - (deltaY - deltaY/numberPoints*it))
        } as ArrayList<Point>
        if (points.isEmpty()) Toast.makeText(context, "i'm a null", Toast.LENGTH_SHORT).show()
        shape.points += points
        if (event.actionMasked == MotionEvent.ACTION_UP) {
            listeners.forEach { it(shape) }
        }
        preView = loadBitmapFromView()
        invalidate()
        return true
    }

    fun setOnCreateShapeView(value: (v:View) -> Unit) = listeners.add(value)

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        val fuckPoints = getFuckPoints()
        fuckPoints.forEach {
            brush.setBounds(it.x - 5, bottom - it.y - 5, it.x + 5, bottom - it.y + 5)
            brush.draw(canvas)
        }
//        if (preView != null) canvas.drawBitmap(preView!!, 0f, 0f, null)
        points.forEach {
            brush.setBounds(it.x - 5, it.y - 5, it.x + 5, it.y + 5)
            brush.draw(canvas)
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