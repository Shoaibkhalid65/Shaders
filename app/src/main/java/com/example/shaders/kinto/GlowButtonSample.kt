package com.example.shaders.kinto

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.example.shaders.timo.setColorUniform
import com.example.shaders.ui.theme.LightYellow
import org.intellij.lang.annotations.Language


@Language("AGSL")
val glowButtonShader = """
  // Shader for a glowing rounded rectangle button

//  uniform shader button;              // Input texture or color for the button
  uniform float2 size;                // Button size
  uniform float cornerRadius;         // Corner radius of the button
  uniform float glowRadius;           // Radius of the glow effect
  uniform float glowIntensity;        // Intensity of the glow
  layout(color) uniform half4 glowColor; // Color of the glow

  // Signed Distance Function (SDF) for a rounded rectangle
  float calculateRoundedRectSDF(vec2 position, vec2 rectSize, float radius) {
      vec2 adjustedPosition = abs(position) - rectSize + radius; // Adjust for rounded corners
      return min(max(adjustedPosition.x, adjustedPosition.y), 0.0) 
             + length(max(adjustedPosition, 0.0)) - radius;
  }

  // Function to calculate glow intensity based on distance
  float calculateGlow(float distance, float radius, float intensity) {
      return pow(radius / distance, intensity); // Glow falls off as distance increases
  }

  half4 main(float2 coord) {
      // Normalize coordinates and aspect ratio
      float aspectRatio = size.y / size.x;
      float2 normalizedPosition = coord.xy / size;
      normalizedPosition.y *= aspectRatio;

      // Define normalized rectangle size and center
      float2 normalizedRect = float2(1.0, aspectRatio);
      float2 normalizedRectCenter = normalizedRect / 2.0;
      normalizedPosition -= normalizedRectCenter;

      // Calculate normalized corner radius and distance
      float normalizedRadius = aspectRatio / 2.0;
      float distanceToRect = calculateRoundedRectSDF(normalizedPosition, normalizedRectCenter, normalizedRadius);

      // Get the button's color
      half4 buttonColor = half4(0.1, 0.8, 0.1, 1.0);

      // Inside the rounded rectangle, return the button's original color
      if (distanceToRect < 0.0) {
        return buttonColor;
      }

      // Outside the rectangle, calculate glow effect
      float glow = calculateGlow(distanceToRect, glowRadius, glowIntensity);
      half4 glowEffect = glow * glowColor;

      // Apply tone mapping to the glow for a natural look
      glowEffect = 1.0 - exp(-glowEffect);

      return glowEffect;
  }
""".trimIndent()

@Composable
fun GlowButtonSample() {
    val shader = remember { RuntimeShader(glowButtonShader) }
    val shaderBrush = remember { ShaderBrush(shader) }
    Box(
        modifier = Modifier.fillMaxSize().background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .size(150.dp,50.dp)
                .onSizeChanged { size ->
                    shader.setFloatUniform("size", size.width.toFloat(), size.height.toFloat())
                    shader.setFloatUniform("cornerRadius", 32f)
                    shader.setFloatUniform("glowRadius",300f)
                    shader.setFloatUniform("glowIntensity", 2f)
                    shader.setColorUniform("glowColor", Color.Green.toArgb())
                }
                .background(shaderBrush)
        )
    }
}