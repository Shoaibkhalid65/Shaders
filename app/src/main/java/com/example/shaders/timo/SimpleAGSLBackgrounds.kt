package com.example.shaders.timo


import android.graphics.RuntimeShader
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import org.intellij.lang.annotations.Language

@Language("AGSL")
val shaderCode = """
    float3 color1 = float3(0.1);
    float3 color2 = float3(0.9);
    
    float3 checkerBoard(float2 uv) {
        float2 id = floor(uv);
        float w = fract((id.x + id.y) / 2.0) * 2.0;
        return mix(color1.rgb, color2.rgb, w);
    }
    
    float4 main(float2 fragCoord) {
        return float4(checkerBoard(fragCoord / 5.0), 1);
    }
""".trimIndent()


fun RuntimeShader.setColorUniform(uniformName: String, color: Color) =
    setColorUniform(uniformName, color.toArgb())
//setFloatUniform(uniformName, color.red, color.green, color.blue, color.alpha)

fun Modifier.backgroundShader(
    shaderSrc: String,
    background: Color? = null,
    primary: Color? = null
): Modifier = this.drawWithCache {
    val shader = RuntimeShader(shaderSrc)
    val brush = ShaderBrush(shader)
    if (background != null) {
        shader.setColorUniform("background", background)
    }
    if (primary != null) {
        shader.setColorUniform("primary", primary)
    }
    onDrawBehind {
        drawRect(brush)
    }
}

@Composable
fun BackgroundShader(
    modifier: Modifier = Modifier,
    shaderSrc: String,
    background: Color? = null,
    primary: Color? = null
) {

    Spacer(
        modifier = modifier.drawWithCache {
            val shader = RuntimeShader(shaderSrc)
            val brush = ShaderBrush(shader)
            if (background != null) {
                shader.setColorUniform("background", background)
            }
            if (primary != null) {
                shader.setColorUniform("primary", primary)
            }
            onDrawBehind {
                drawRect(brush)
            }
        }
    )

}


@Language("AGSL")
val shaderAOABackground = """
layout(color) uniform vec4 background;
layout(color) uniform vec4 primary;

float PI = 3.14159265359;

float dSine(vec2 uv, vec2 offset, float amplitude) {
    float a = 2.5;
    return uv.y + offset.y - cos((sin(uv.x*a*1.1)*a + PI + offset.x)*1.) * amplitude * 1.6;
}

float dMainLine(vec2 uv, vec2 offset, float amplitude) {
    float diff = dSine(uv, offset, amplitude);
    float d = min(smoothstep(0.15, 0.0, diff), smoothstep(-0.005, 0.0, diff));
    return clamp(d, 0.0, 1.0);
}

float dMaskLine(vec2 uv, vec2 offset, float amplitude) {
    float diff = dSine(uv, offset, amplitude);
    float d = min(smoothstep(.2, 0.0, diff), smoothstep(-.2, 0.0, diff));
    return clamp(d, 0.0, 1.0);
}

const float width = 0.01;

vec3 Grid(vec2 uv) {
    vec3 col = vec3(0);
    if(abs(uv.x) < uv.x + width) col.g = 1.;
    if(abs(uv.y) < uv.y + width) col.r = 1.;
    vec2 grid = 1.-abs(fract(uv)-.5)*2.;
    grid = smoothstep(grid + width, vec2(0), grid);
    col += (grid.x+grid.y)*.5;
    return col*.5;
}

float Line(in vec2 p, in vec2 a, in vec2 b, float w) {
    vec2 pa = p - a, ba = b - a;
    float h = clamp(dot(pa,ba) / dot(ba,ba), 0., 1.);
    float d = length(pa - ba * h);
    return smoothstep(w, w-d+width, d);
}

half4 main(vec2 fragcoord) {
    vec2 uv = (fragcoord) / 1100.;
    uv *= vec2(.25, -1.);
    uv *= 1.5;
    float freq = 2.;
    float id = floor(uv.y / freq);
    uv.y = mod(uv.y, freq)-.5*2.;

    float d = 0.0;
    float mask = 0.0;

    uv.x += 5.*id;

    d = max(d, dMainLine(uv, vec2(.0, .0), 0.3)*0.8);
    d = max(d, dMainLine(uv, vec2(-.03, .033), 0.33)*0.6);
    mask = max(mask, dMaskLine(uv, vec2(.0, .0), 0.3)*0.8);
    float bshift = .2;
    d = max(d, dMainLine(uv, vec2(-.2, .10), 0.3)*0.3);
    mask = max(mask, dMaskLine(uv, vec2(-.2, .10), 0.3)*0.3);
    d = max(d, dMainLine(uv, vec2(-.25, .15), 0.34)*0.2);

    //d = clamp(d, .0, 1.);

    vec3 colLine = mix(vec3(1.), primary.rgb, d);
    vec3 col = mix(background.rgb, colLine, mask);
    return vec4(col, 1);
}    
""".trimIndent()

@Composable
fun ExperimentSamples() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .backgroundShader(shaderAOABackground, Color(0xFFf05053), Color(0xFFe1eec3))

    ) {

    }
}