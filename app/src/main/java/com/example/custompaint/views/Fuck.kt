package com.example.custompaint.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.custompaint.R

class Fuck(context: Context, attr: AttributeSet) : View(context, attr)  {

    var points: ArrayList<Point> = arrayListOf()
    private val brush = ContextCompat.getDrawable(context, R.drawable.point)!!

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        points.forEach {
            brush.setBounds(it.x - 5, it.y - 5, it.x + 5, it.y + 5)
            brush.draw(canvas)
        }
    }
}