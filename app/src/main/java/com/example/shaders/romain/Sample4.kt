package com.example.shaders.romain

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shaders.R
import kotlinx.coroutines.delay

// snow sample
@Composable
fun Sample4() {
    val shaderCode = """
        uniform float2 iResolution;
        uniform float iTime;

        const float _NUMSHEETS = 4.0;
        const float _NUMFLAKES = 60.0;
        float2 uv;

        float rnd(float x) {
            return fract(sin(dot(float2(x + 47.49, 38.2467 / (x + 2.3)), float2(12.9898, 78.233))) * 43758.5453);
        }

        float drawFlake(float2 center, float radius) {
            // returns brightness of a flake at given position
            return 1.0 - sqrt(smoothstep(0.0, radius, length(uv - center)));
        }

        half4 main(float2 fragCoord) {
            uv = fragCoord / iResolution.x;

            // ✅ Start with transparent background
            float3 col = float3(0.0);

            float alpha = 0.0; // track opacity of flakes

            for (float i = 1.0; i <= _NUMSHEETS; i++) {
                for (float j = 1.0; j <= _NUMFLAKES; j++) {
                    if (j > _NUMFLAKES / i) break;

                    float size = 0.01 * i * (1.0 + rnd(j) / 2.0);
                    float speed = size * 0.75 + rnd(i) / 1.5;
                    float2 center;
                    center.x = -0.3 + rnd(j * i) * 1.4 + 0.1 * cos(iTime + sin(j * i));
                    center.y = fract(sin(j) - speed * iTime) / 1.3;
//                    center.y = 1.0 - fract(sin(j) + speed * iTime) / 1.3;

                    float flake = (1.0 - i / _NUMSHEETS) * drawFlake(center, size);

                    // Add white snowflake color
                    col += flake * float3(1.0, 1.0, 1.0);

                    // Increase opacity where snowflake exists
                    alpha += flake;
                }
            }

            // Clamp alpha so it doesn’t exceed 1
            alpha = clamp(alpha, 0.0, 1.0);

            // ✅ Return with transparent background
            return half4(col, alpha);
        }
    """.trimIndent()


    val shader = remember { RuntimeShader(shaderCode) }
    var time by remember { mutableFloatStateOf(0f) }

    // Animate time
    LaunchedEffect(Unit) {
        while (true) {
            time += 0.016f // roughly 60 FPS
            delay(16)
        }
    }
    LaunchedEffect(time) {
        shader.setFloatUniform("iTime", time)
    }
    val shaderBrush = ShaderBrush(shader)


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        Image(
            painter = painterResource(R.drawable.a1),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(300.dp)
                .clip(MaterialTheme.shapes.extraLarge),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(300.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .onSizeChanged { size ->
                    shader.setFloatUniform(
                        "iResolution",
                        size.width.toFloat(),
                        size.height.toFloat()
                    )
                }
                .background(shaderBrush)
                .rotate(270f)
        ) { }
    }
}
