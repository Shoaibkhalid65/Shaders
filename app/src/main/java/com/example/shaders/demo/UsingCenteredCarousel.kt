package com.example.shaders.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shaders.kinto.RainEffectBox
import com.example.shaders.kinto.RippleEffectSample
import com.example.shaders.kinto.RippleEffectSampleBox
import com.example.shaders.romain.SnowEffectSampleBox
import com.example.shaders.timo.backgroundShader
import com.example.shaders.timo.shaderAOABackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun UsingCenteredCarousel() {
    val carouselState= rememberCarouselState{3}
    Box(
        modifier = Modifier
            .fillMaxSize()
            .backgroundShader(
                shaderAOABackground,
                primary = Color(0xFF34e89e),
                background = Color(0xFF0f3443)
            ),
        contentAlignment = Alignment.Center
    ){
        HorizontalCenteredHeroCarousel(state = carouselState, itemSpacing = 12.dp, contentPadding = PaddingValues(16.dp)) { i->
            when(i){
                1-> SnowEffectSampleBox(modifier = Modifier.maskClip(MaterialTheme.shapes.extraLarge))
                0-> RainEffectBox(modifier = Modifier.maskClip(MaterialTheme.shapes.extraLarge))
                else -> RippleEffectSampleBox(modifier = Modifier.maskClip(MaterialTheme.shapes.extraLarge))
            }
        }
    }
}