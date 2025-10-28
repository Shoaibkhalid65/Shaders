package com.example.shaders.romain

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.example.shaders.R
import org.intellij.lang.annotations.Language

@Language("AGSL")
const val ShaderSource = """
uniform shader composable;
uniform float2 size;
uniform float amount;
half4 main(float2 fragCoord){
float distance=length(fragCoord-size*0.5);

half4 color=composable.eval(fragCoord);

return half4(distance/max(size.x,size.y),0,0,1);
}
    
"""

@Composable
fun Sample1(resources: Resources) {
    val photo = BitmapFactory.decodeResource(resources, R.drawable.moutain)
    val shader = RuntimeShader(ShaderSource)
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        Image(
            modifier = Modifier
                .onSizeChanged{intSize ->
                    shader.setFloatUniform("size",intSize.width.toFloat(),intSize.height.toFloat())
                }
                .graphicsLayer {
                clip=true
                renderEffect = RenderEffect.createRuntimeShaderEffect(
                    shader, "composable"
                ).asComposeRenderEffect()
            },
            bitmap = photo.asImageBitmap(),
            contentDescription = "image"
        )
    }
}