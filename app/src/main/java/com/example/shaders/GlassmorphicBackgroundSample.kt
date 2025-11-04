

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.shaders.R  // Replace with your actual package R reference

// ===================== Simple Glass Background Shader =====================
private const val GlassBackgroundAGSL = """
uniform shader image;
uniform float2 resolution;
uniform float3 bgColor;
uniform float  bgAlpha;

half4 main(float2 fragCoord) {
    // Sample the base image
    half4 base = image.eval(fragCoord);

    // Blend the base image with a soft colored tint
    float3 mixed = mix(base.rgb, bgColor, bgAlpha);

    // Optional subtle vignette (slightly darker corners)
    float2 uv = fragCoord / resolution;
    float vignette = smoothstep(0.0, 0.8, distance(uv, float2(0.5, 0.5)));
    mixed *= (1.0 - vignette * 0.1);

    return half4(mixed, base.a);
}
"""

// ===================== Composable =====================
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun GlassyBackground(
    modifier: Modifier = Modifier,
    width: Dp = 300.dp,
    height: Dp = 160.dp,
    bgColor: Color = Color.White,
    bgAlpha: Float = 0.3f
) {
    val shader = remember { RuntimeShader(GlassBackgroundAGSL) }
    var renderSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .onSizeChanged { renderSize = it }
            .graphicsLayer {
                val w = renderSize.width.takeIf { it > 0 } ?: 1
                val h = renderSize.height.takeIf { it > 0 } ?: 1

                shader.setFloatUniform("resolution", w.toFloat(), h.toFloat())
                shader.setFloatUniform("bgColor", bgColor.red, bgColor.green, bgColor.blue)
                shader.setFloatUniform("bgAlpha", bgAlpha)

                renderEffect = RenderEffect
                    .createRuntimeShaderEffect(shader, "image")
                    .asComposeRenderEffect()
            }
    )
}

// ===================== Preview =====================
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun PreviewGlassyBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.a2),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Glass overlay
        GlassyBackground(
            width = 340.dp,
            height = 160.dp,
            bgColor = Color.White,
            bgAlpha = 0.25f
        )
    }
}
