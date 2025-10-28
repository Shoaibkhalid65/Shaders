package com.example.shaders.rinkin

import android.graphics.RuntimeShader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.onSizeChanged
import org.intellij.lang.annotations.Language

@Composable
fun BasicSample(){
    @Language("AGSL")
    val BASIC_SAMPLE="""
        uniform float2 resolution;
        half4 main(float2 coord){
        float2 uv= coord.xy/resolution;
        return half4(uv.x,uv.y,1.0,1.0);
        }
    """.trimIndent()
    val shader = remember { RuntimeShader(BASIC_SAMPLE) }
    var size by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(size) {
        shader.setFloatUniform("resolution",size.width,size.height)
    }
    val shaderBrush= remember(shader,size) {
        ShaderBrush(shader = shader)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged{
                size=Size(it.width.toFloat(),it.height.toFloat())
            }
            .background(brush = shaderBrush)
    )
}