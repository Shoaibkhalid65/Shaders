package com.example.shaders.timo

import android.graphics.RuntimeShader
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.Language

@Language("AGSL")
const val button_shine_shader="""
    uniform float2 iResolution;
uniform float iTime;

half4 main(vec2 fragCoord) {
    fragCoord /= iResolution.x; // scale to device width
    float timePos = iTime * 3.0;
    float width = 0.1;
    float pos = fragCoord.x + fragCoord.y * .4;
    float highLight = smoothstep(timePos -width, timePos, pos);
    highLight -= smoothstep(timePos, timePos + width, pos);
    //highLight = max(0.0, highLight);
    vec3 col = vec3(.8, 0.2, 0.5) * highLight;
    return vec4(col, highLight);
}
"""
@Composable
fun ButtonShineScreen(){
    val shader= remember { RuntimeShader(button_shine_shader) }
    val brush= remember { ShaderBrush(shader) }
    val time by  produceState(0f) {
        while (true){
            withInfiniteAnimationFrameMillis {
                value=it/1000f
            }
        }
    }
    shader.setFloatUniform("iTime",time)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier.size(100.dp).onSizeChanged{
                shader.setFloatUniform("iResolution",it.width.toFloat(),it.height.toFloat())
            }.background(brush)
        )
    }
}