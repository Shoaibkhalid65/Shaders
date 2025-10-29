package com.example.shaders.timo

import android.graphics.RuntimeShader
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.Language

@Composable
fun BasicSamples() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        BasicBox()
    }
}

@Language("AGSL")
const val FIRST_SOURCE = """
    uniform float2 resolution;
    half4 main(float2 fragCoord){
       float2 uv=fragCoord/resolution;
       float g=smoothstep(0.0,1.0,uv.y);
       float r=smoothstep(0.3,0.7,uv.x);
       float b=smoothstep(0.9,0.1,uv.y);
       return half4(r,g,b,1.0);
    }
"""

@Language("AGSL")
const val SECOND_SOURCE = """
    uniform float2 iResolution;
    uniform float iTime;
    float4 main(float2 fragCoord){
    fragCoord/=iResolution.x;
    float timePos= iTime*3.0;
    float width=0.1;
    float pos=fragCoord.x + fragCoord.y * .4;
    float highLight=min(1.0,smoothstep(timePos-width,timePos,pos));
    highLight -= smoothstep(timePos,timePos+width,pos);
    highLight=max(0.0,highLight);
    float3 col=float3(highLight);
    return float4(col,highLight);
    }
"""

fun Modifier.backgroundShader(shaderSource: String, time: Float): Modifier =
    this.drawWithCache {
        val shader = RuntimeShader(shaderSource)
        shader.setFloatUniform("iResolution", size.width, size.height)
        shader.setFloatUniform("iTime", time)
        val shaderBrush = ShaderBrush(shader)
        onDrawBehind {
            drawRect(brush = shaderBrush)
        }
    }

@Composable
fun BasicBox() {
    val time by produceState(0f) {
        value = withInfiniteAnimationFrameMillis { it / 1000f }
    }
    Box(
        modifier = Modifier
            .size(200.dp)
            .backgroundShader(SECOND_SOURCE,time)
    )
}

@Composable
fun BoxGradientShaderExample() {
    val shader = remember {
        RuntimeShader(
            """
            uniform float2 resolution;
            uniform float time;

            float3 msign(float3 p) {
                return sign(p);
            }

            float3 boxGradient(float3 p, float3 rad) {
                float3 d = abs(p) - rad;
                float3 s = msign(p);
                float g = max(max(d.x, d.y), d.z);
                
                return s * ((g > 0.0)
                    ? normalize(max(d, 0.0))
                    : step(d.yzx, d.xyz) * step(d.zxy, d.xyz));
            }

            half4 main(float2 fragCoord) {
                float2 uv = (fragCoord - 0.5 * resolution) / resolution.y;
                float3 p = float3(uv, sin(time * 0.5));
                float3 boxSize = float3(0.3, 0.3, 0.3);
                float3 grad = boxGradient(p, boxSize);
                float3 color = 0.5 + 0.5 * grad;
                return half4(color, 1.0);
            }
            """.trimIndent()
        )
    }

    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            time += 0.03f
            delay(16)
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        shader.setFloatUniform("resolution", size.width, size.height)
        shader.setFloatUniform("time", time)
        drawRect(brush = ShaderBrush(shader))
    }
}

@Composable
fun MovingHighlightShader() {
    val shader = remember {
        RuntimeShader(SECOND_SOURCE)
    }

    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            time += 0.02f
            delay(16)
        }
    }


}



