package com.example.shaders.romain

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.Language
import java.nio.file.WatchEvent

@Language("AGSL")
const val TEXT_SHADER_SOURCE="""
    uniform float2 resolution; // Text size
  uniform float time;        // Time for animation
  uniform shader composable; // Input composable (text mask)

  half4 main(float2 coord) {
      // Normalize coordinates to [0, 1]
      float2 uv = coord / resolution;

      // Hardcoded gradient colors
      half4 startColor = half4(1.0, 0.65, 0.15, 1.0); // Orange
      half4 endColor = half4(0.26, 0.65, 0.96, 1.0);  // Blue

      // Linear gradient from startColor to endColor
      half4 gradientColor = mix(startColor, endColor, uv.x);

      // Optional: Add a subtle animation (gradient shifting)
      float shift = 0.5 + 0.5 * sin(time * 2.0);
      gradientColor = mix(startColor, endColor, uv.x + shift * 0.1);

      // Use the alpha from the input composable mask
      half4 textAlpha = composable.eval(coord);

      // Combine the gradient color with the composable alpha
      return gradientColor * textAlpha.a;
  }
"""
@Composable
fun Sample3(){
    val shader = remember { RuntimeShader(TEXT_SHADER_SOURCE) }
    var time by remember { mutableFloatStateOf(0f) }

    // Increment animation time
    LaunchedEffect(Unit) {
        while (true) {
            time += 0.016f // Simulate 60 FPS
            delay(16)
        }
    }
    shader.setFloatUniform("time", time)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "Javed Iqbal",
            modifier = Modifier
                .onSizeChanged{intSize ->
                    shader.setFloatUniform("resolution",intSize.width.toFloat(),intSize.height.toFloat())
                }
                .graphicsLayer{
                    renderEffect= RenderEffect.createRuntimeShaderEffect(
                        shader,"composable"
                    ).asComposeRenderEffect()
                }
        )
    }
}