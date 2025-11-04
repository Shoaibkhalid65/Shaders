package com.example.shaders.kinto

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shaders.R
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.Language


@Language("AGSL")
val rippleShader = """
    // Uniform variables: inputs provided from the outside
    uniform float2 size;       // The size of the canvas in pixels (width, height)
    uniform float time;        // The elapsed time for animating the ripple effect
    uniform shader composable; // The shader applied to the composable content being rendered
    
    // Main function: calculates the final color at a given fragment (pixel) coordinate
    half4 main(float2 fragCoord) {
        // Scale factor based on the canvas width for normalization
        float scale = 1 / size.x;
        
        // Normalize fragment coordinates
        float2 scaledCoord = fragCoord * scale;
        
        // Calculate the center of the canvas in normalized coordinates
        float2 center = size * 0.5 * scale;
        
        // Calculate the distance from the current fragment to the center
        float dist = distance(scaledCoord, center);
        
        // Calculate the direction vector from the center to the fragment
        float2 dir = scaledCoord - center;
        
        // Apply a sinusoidal wave based on the distance and time
        float sin = sin(dist * 70 - time * 6.28);
        
        // Offset coordinates by applying the wave effect in the direction of the fragment
        float2 offset = dir * sin;
        
        // Calculate the texture coordinates with the ripple effect applied
        float2 textCoord = scaledCoord + offset / 30;
        
        // Sample the composable shader using the adjusted texture coordinates
        return composable.eval(textCoord / scale);
    }
""".trimIndent()

fun Modifier.gradientTextEffect1(): Modifier = composed {
    val shader = remember { RuntimeShader(rippleShader) }
    var time by remember { mutableFloatStateOf(0f) }

    // Increment animation time
    LaunchedEffect(Unit) {
        while (true) {
            time += 0.016f // Simulate 60 FPS
            delay(16)
        }
    }

    this.graphicsLayer {
        shader.setFloatUniform("size", size.width, size.height)
        shader.setFloatUniform("time", time)

        renderEffect = RenderEffect
            .createRuntimeShaderEffect(shader, "composable")
            .asComposeRenderEffect()
    }
}

@Composable
fun RippleEffectSample(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(R.drawable.che_pic),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 24.dp).size(300.dp).gradientTextEffect1()
        )
    }
}

@Composable
fun RippleEffectSampleBox(modifier: Modifier= Modifier){
    Image(
        painter = painterResource(R.drawable.a2),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier.height(300.dp).fillMaxWidth().gradientTextEffect1()
    )
}