package com.example.shaders



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shaders.demo.UsingCenteredCarousel
import com.example.shaders.romain.Sample4
import com.example.shaders.timo.ExperimentSamples


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UsingCenteredCarousel()

        }
    }
}

