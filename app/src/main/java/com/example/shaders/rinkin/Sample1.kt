package com.example.shaders.rinkin

import android.graphics.RuntimeShader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.Language


@Language("AGSL")
const val SHADER_CODE_BOX = """
    float3 color1=float3(0.1,0.2,0.07);
    float3 color2=float3(0.9,0.78,0.94);
    float3 checkerBoard(float2 uv){
          float2 id=floor(uv);
          float w= fract((id.x+id.y)/2.0)*2.0;
          return mix(color1.rgb,color2.rgb,w);
    }
    half4 main (float2 fragCoord){
         return half4(checkerBoard(fragCoord/8.0),1.0);
    }
"""

@Composable
fun Sample1() {
    val shader = remember { RuntimeShader(SHADER_CODE_BOX) }
    val shaderBrush = remember { ShaderBrush(shader) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = shaderBrush),
        contentAlignment = Alignment.Center
    ){
        Button(onClick = {}, modifier = Modifier.padding(24.dp
        )) { Text("Hello Shoaib") }
    }
}