package com.example.shaders


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

import com.example.shaders.kinto.GlowButtonSample
import com.example.shaders.kinto.RainEffectSample
import com.example.shaders.kinto.RippleEffectSample


import com.example.shaders.rinkin.Sample1
import com.example.shaders.romain.Sample4
import com.example.shaders.timo.ButtonShineScreen
import com.example.shaders.timo.ExperimentSamples
import com.example.shaders.timo.FlameScreen
import com.example.shaders.timo.TransitionScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sample4()

        }
    }
}

