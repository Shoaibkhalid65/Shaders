package com.example.shaders.romain

import android.graphics.RuntimeShader
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.onSizeChanged

const val SHADER_SOURCE = """
    uniform float2 resolution;
    uniform float time;
    float f(float3 p) {
    p.z -= time * 10.;
    float a = p.z * .1;
    p.xy *= mat2(cos(a), sin(a), -sin(a), cos(a));
    return .1 - length(cos(p.xy) + sin(p.yz));
}

half4 main(float2 fragcoord) { 
    float3 d = .5 - fragcoord.xy1 / resolution.y;
    float3 p=float3(0);
    for (int i = 0; i < 32; i++) {
      p += f(p) * d;
    }
    return ((sin(p) + float3(2, 5, 12)) / length(p)).xyz1;
}
"""



@Composable
fun Sample2() {
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f
            }
        }
    }

    val shader = RuntimeShader(SHADER_SOURCE)
    val shaderBrush = ShaderBrush(shader)
    shader.setFloatUniform("time",time)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged {
                shader.setFloatUniform("resolution", it.width.toFloat(), it.height.toFloat())
            }
            .background(brush = shaderBrush)
    )
}