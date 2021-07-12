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
import kotlin.math.sqrt


class Canvas(context: Context, attr: AttributeSet) : View(context, attr) {

    private var points: ArrayList<Point> = arrayListOf()
    private val pointDrawable = ContextCompat.getDrawable(context, R.drawable.point)!!
    private var preView: Bitmap? = null

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        if (event.actionMasked == MotionEvent.ACTION_DOWN || points.isEmpty()) {
            points.clear()
            points.add(Point(event.x.toInt(), event.y.toInt()))
            return true
        }
        val lastPoint = points.last()
        val deltaX = event.x.toInt() - lastPoint.x
        val deltaY = event.y.toInt() - lastPoint.y
        val distance = sqrt((deltaX*deltaX + deltaY*deltaY).toFloat())
        val numberPoints = (distance/5).toInt()
        points = (1..numberPoints).map {
            Point(event.x.toInt() - (deltaX - deltaX/numberPoints*it),
                event.y.toInt() - (deltaY - deltaY/numberPoints*it))
        } as ArrayList<Point>
        preView = loadBitmapFromView()
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        if (preView != null) canvas.drawBitmap(preView!!, 0f, 0f, null)
        points.forEach {
            pointDrawable.setBounds(it.x - 5, it.y - 5, it.x + 5, it.y + 5)
            pointDrawable.draw(canvas)
        }
    }

    fun loadBitmapFromView(): Bitmap {
        measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val b = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        layout(left, top, right, bottom)
        draw(c)
        return b
    }

}