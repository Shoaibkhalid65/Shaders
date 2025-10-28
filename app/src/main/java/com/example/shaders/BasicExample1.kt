package com.example.shaders

import android.graphics.Color
import android.graphics.RuntimeShader
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ShaderBrush
import com.example.shaders.ui.theme.Coral
import com.example.shaders.ui.theme.LightYellow
import org.intellij.lang.annotations.Language

@Composable
fun BasicExample1(){
    @Language("AGSL")
    val CUSTOM_SHADER="""
        uniform float time;
        uniform float2 resolution;
        layout(color) uniform half4 color;
        layout(color) uniform half4 color2;
        half4 main(in float2 fragCoord){
         float2 uv=fragCoord/resolution.xy;
         float mixValue=distance(uv,vec2(0,1))+abs(sin(time * 0.5));
         return mix(color,color2,mixValue);
        }
    """.trimIndent()
    val time by produceState(0f) {
        while (true){
            withInfiniteAnimationFrameMillis {
                value=it/1000f
            }
        }
    }
    Box(

        modifier = Modifier
            .drawWithCache{
                val shader = RuntimeShader(CUSTOM_SHADER)
                val shaderBrush= ShaderBrush(shader = shader)
                shader.setFloatUniform("resolution",size.width,size.height)

                onDrawBehind {
                    shader.setFloatUniform("time",time)
                    shader.setColorUniform(
                        "color", Color.valueOf(LightYellow.red,LightYellow.green,LightYellow.blue,LightYellow.alpha)
                    )
                    shader.setColorUniform(
                        "color2",
                        Color.valueOf(Coral.red,Coral.green,Coral.blue,Coral.alpha)
                    )
                    drawRect(shaderBrush)
                }
            }
            .fillMaxSize()
    )
}