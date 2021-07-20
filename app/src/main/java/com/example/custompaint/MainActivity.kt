package com.example.custompaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.example.custompaint.databinding.ActivityMainBinding
import com.example.custompaint.views.Canvas

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.canvas.setOnCreateShapeView { binding.root.addView(it) }
    }
}