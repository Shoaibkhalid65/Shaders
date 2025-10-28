package com.example.shaders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shaders.rinkin.BasicSample
import com.example.shaders.romain.Sample1
import com.example.shaders.romain.Sample2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sample2()

        }
    }
}

