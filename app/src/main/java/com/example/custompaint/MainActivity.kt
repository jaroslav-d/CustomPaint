package com.example.custompaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.custompaint.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}