package com.example.custompaint.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.custompaint.R


class Canvas(context: Context, attr: AttributeSet) : View(context, attr) {

    private val points: ArrayList<Drawable> = arrayListOf()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
//        if (points.isEmpty()) {
//            val point = ContextCompat.getDrawable(context, R.drawable.point)!!
//            point.setBounds(event.x.toInt() - 5, event.y.toInt() - 5, event.x.toInt() + 5, event.y.toInt() + 5)
//            points.add(point)
//            return true
//        }
//        val lastPoint = points.last()
//        val deltaX = event.x.toInt() - lastPoint.bounds.left + 5
//        val deltaY = event.y.toInt() - lastPoint.bounds.top + 5
//        points += (1..10).map {
//            ContextCompat.getDrawable(context, R.drawable.point)!!.apply {
//                setBounds(
//                    event.x.toInt() - (deltaX - deltaX/10*it) - 5,
//                    event.y.toInt() - (deltaY - deltaY/10*it) - 5,
//                    event.x.toInt() - (deltaX - deltaX/10*it) + 5,
//                    event.y.toInt() - (deltaY - deltaY/10*it) + 5)
//            }
//        }
        val point = ContextCompat.getDrawable(context, R.drawable.point)!!
        point.setBounds(event.x.toInt() - 5, event.y.toInt() - 5, event.x.toInt() + 5, event.y.toInt() + 5)
        points.add(point)
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        points.forEach { it.draw(canvas!!) }
    }

}