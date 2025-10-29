package com.example.shaders


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shaders.rinkin.BasicSample
import com.example.shaders.romain.Sample1
import com.example.shaders.romain.Sample2
import com.example.shaders.romain.Sample3
import com.example.shaders.romain.Sample4
import com.example.shaders.timo.BasicSamples
import com.example.shaders.timo.BoxGradientShaderExample
import com.example.shaders.timo.ExperimentSamples
import com.example.shaders.timo.FlameScreen
import com.example.shaders.timo.LoadingSpinner
import com.example.shaders.timo.MovingHighlightShader
import com.example.shaders.timo.NoiceBackgroundBox


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExperimentSamples()

        }
    }
}

