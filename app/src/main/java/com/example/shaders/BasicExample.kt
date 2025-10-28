package com.example.shaders

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.Language


@Language("AGSL")
val BASIC_SHADER = """
        half4 main (in float2 fragCord){
              return half4(1,0.67,1,1);
        }
    """.trimIndent()
@Language("AGSL")
val shaderCode = """
uniform float2 resolution;
uniform float2 mouse;
uniform float time;

float plot(vec2 st, float pct) {
    return smoothstep(pct - 0.01, pct, st.y) -
           smoothstep(pct, pct + 0.01, st.y);
}

float random(float x) {
    return fract(sin(x) * 43758.5453);
}

vec4 main(vec2 fragCoord) {
    vec2 st = fragCoord / resolution;
    st.x *= resolution.x / resolution.y;
    vec3 color = vec3(0.0);

    float y = random(st.x * 0.001 + time);
    float pct = plot(st, y);

    color = mix(color, vec3(0.0, 1.0, 0.0), pct);
    return vec4(color, 1.0);
}
""".trimIndent()

@Language("AGSL")
val shaderCode1 = """
uniform float2 resolution;
uniform float2 mouse;
uniform float time;

const float rows = 10.0;

float circle(vec2 st, float radius) {
    vec2 pos = vec2(0.5) - st;
    radius *= 0.75;
    return 1.0 - smoothstep(
        radius - (radius * 0.01),
        radius + (radius * 0.01),
        dot(pos, pos) * 3.14
    );
}

float random(vec2 st) {
    return fract(sin(dot(st, vec2(12.9898, 78.233))) * 43758.5453);
}

vec4 main(vec2 fragCoord) {
    vec2 st = fragCoord / resolution;
    st.x *= resolution.x / resolution.y;

    st *= rows;

    // Offset every other row
    st.x -= step(1.0, mod(st.y, 2.0)) * 0.5;

    vec2 ipos = floor(st);  // integer position
    vec2 fpos = fract(st);  // fractional position

    // Move Right
    ipos += vec2(floor(time * -8.0), 0.0);

    float pct = random(ipos);
    pct *= circle(fpos, 0.5);

    // Optional interaction with mouse:
    // pct = step(0.1 + mouse.x / resolution.x, pct);
    // pct = 1.0 - pct;

    return vec4(vec3(pct), 1.0);
}
""".trimIndent()

@Language("Agsl")
val shaderCode2 = """
uniform float2 resolution;
uniform float2 mouse;
uniform float time;

float plot(vec2 st, float pct) {
    return smoothstep(pct - 0.01, pct, st.y) -
           smoothstep(pct, pct + 0.01, st.y);
}

float random(float x) {
    return fract(sin(x) * 1e4);
}

float noise(float x) {
    float i = floor(x);
    float f = fract(x);

    // Cubic Hermite curve
    float u = f * f * (3.0 - 2.0 * f);

    return mix(random(i), random(i + 1.0), u);
}

vec4 main(vec2 fragCoord) {
    vec2 st = fragCoord / resolution;
    st.x *= resolution.x / resolution.y;

    vec3 color = vec3(0.0);

    float y = noise(st.x * 3.0 + time);

    float pct = plot(st, y);
    color = mix(color, vec3(0.0, 1.0, 0.0), pct);

    return vec4(color, 1.0);
}
""".trimIndent()




@Composable
@Preview(showBackground = true)
fun BasicExample() {
    Box(
        modifier = Modifier
            .drawWithCache {
                val shader = RuntimeShader(BASIC_SHADER)
                val shaderBrush = ShaderBrush(shader)
                onDrawBehind {
                    drawRect(shaderBrush)
                }
            }
            .fillMaxSize()
    )
}