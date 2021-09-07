package com.example.custompaint.views

import android.content.Context
import android.graphics.*
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import com.example.custompaint.R

class Fuck(context: Context, attr: AttributeSet? = null) : View(context, attr)  {

    var points: ArrayList<Point> = arrayListOf()
    private val brush = ContextCompat.getDrawable(context, R.drawable.point)!!
    private val bitmap: Bitmap by lazy {
        createBitmap(width, height).applyCanvas {
            points.forEach {
                brush.setBounds(it.x - 5, it.y - 5, it.x + 5, it.y + 5)
                brush.draw(this)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }
}