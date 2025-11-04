package com.example.shaders.kinto

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
val rainShader = """
    uniform float time;        // The elapsed time for animating the rain
    uniform float2 size;       // The size of the canvas in pixels (width, height)
    uniform shader composable; // Shader for the composable content

    // Generate a pseudo-random number based on input
    float random(float st) {
        return fract(sin(st * 12.9898) * 43758.5453123);
    }

    half4 main(float2 fragCoord) {
        // Normalize fragment coordinates to the [0, 1] range
        float2 uv = fragCoord / size;

        // Rain parameters
        float speed = 1.0;             // Speed of raindrops
        float t = time * speed;        // Time-adjusted factor for animation
        float density = 200.0;         // Number of rain "drops" per unit area
        float length = 0.08;            // Length of a raindrop
        float angle = radians(30.0);   // Angle of the rain (in degrees)
        float slope = tan(angle);      // Slope of the rain's trajectory

        // Compute grid position and animated raindrop position
        float gridPosX = floor(uv.x * density);
        float2 pos = -float2(uv.x * density + t * slope, fract(uv.y - t));

        // Calculate the raindrop visibility at this fragment
        float drop = smoothstep(length, 0.0, fract(pos.y + random(gridPosX)));

        // Background and rain colors
        half4 bgColor = half4(0.0, 0.0, 0.0, 0.0);  // Black transparent background
        half4 rainColor = half4(0.8, 0.8, 1.0, 1.0); // Light blue raindrop color

        // Blend the background and raindrop color based on drop visibility
        half4 color = mix(bgColor, rainColor, drop);

        return color; // Output the final color for the fragment
    }
""".trimIndent()

fun Modifier.gradientTextEffect2(): Modifier = composed {
    val shader = remember { RuntimeShader(rainShader) }
    var time by remember { mutableFloatStateOf(0f) }

    // Increment animation time
    LaunchedEffect(Unit) {
        while (true) {
            time += 0.012f // Simulate 60 FPS
            delay(16)
        }
    }

    this.graphicsLayer {
        shader.setFloatUniform("size", size.width, size.height)
        shader.setFloatUniform("time", time)
        clip=true
        renderEffect = RenderEffect
            .createRuntimeShaderEffect(shader, "composable")
            .asComposeRenderEffect()
    }
}
@Composable
fun RainEffectSample(){

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ){
        Box(modifier = Modifier.padding(24.dp).wrapContentSize().clip(MaterialTheme.shapes.extraLarge)) {
            Image(
                painter = painterResource(R.drawable.map_city),
            modifier = Modifier.matchParentSize(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Image(
                painter = painterResource(R.drawable.a1),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.gradientTextEffect2()
            )
        }
    }
}
@Composable
fun RainEffectBox(modifier: Modifier= Modifier){
    Box(modifier = modifier.fillMaxWidth().height(300.dp).clip(MaterialTheme.shapes.extraLarge)) {
        Image(
            painter = painterResource(R.drawable.r1),
            modifier = Modifier.matchParentSize(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(R.drawable.r1),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize().gradientTextEffect2()
        )
    }
}