package com.example.shaders

import android.graphics.RuntimeShader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import org.intellij.lang.annotations.Language
import java.nio.file.WatchEvent


@Composable
fun SampleByGPT(){
    @Language("Agsl")
    val shaderCode = """
uniform float2 resolution;
uniform float time;

// Converts hue to RGB
vec3 hsb2rgb(in vec3 c){
    vec3 rgb = clamp(
        abs(mod(c.x*6.0 + vec3(0.0,4.0,2.0), 6.0) - 3.0) - 1.0,
        0.0,
        1.0
    );
    rgb = rgb * rgb * (3.0 - 2.0 * rgb);
    return c.z * mix(vec3(1.0), rgb, c.y);
}

vec4 main(vec2 fragCoord) {
    vec2 st = fragCoord / resolution.xy;
    st.x *= resolution.x / resolution.y;

    float t = time * 0.5;

    // Create moving plasma pattern
    float colorWave = sin(st.x * 10.0 + t) +
                      sin(st.y * 10.0 + t) +
                      sin((st.x + st.y) * 10.0 + t) +
                      sin(sqrt(st.x * st.x + st.y * st.y) * 20.0 - t);

    colorWave /= 4.0; // normalize
    colorWave = (colorWave + 1.0) * 0.5; // bring to 0–1 range

    // Animate hue based on time and plasma movement
    vec3 color = hsb2rgb(vec3(colorWave + t * 0.05, 1.0, 1.0));

    return vec4(color, 1.0);
}
""".trimIndent()

    var time by remember { mutableFloatStateOf(0f) }

    // Animate the time uniform
    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                time = (it / 1_000_000_000f)
            }
        }
    }

    val shader = remember {
        RuntimeShader(shaderCode)
    }

    val brush = remember(shader, time) {
        shader.setFloatUniform("resolution", 1080f, 2400f)
        shader.setFloatUniform("time", time)
        ShaderBrush(shader)
    }
    Box(
        modifier = Modifier.fillMaxSize().background(brush)
    )

}

@Composable
fun PlasmaShaderEffect() {
    val runtimeShader = remember {
        RuntimeShader(
            """
            uniform float u_time;
            uniform vec2 u_resolution;

            half4 main(vec2 fragCoord) {
                vec2 uv = fragCoord / u_resolution.xy;
                uv = uv * 2.0 - 1.0;
                uv.x *= u_resolution.x / u_resolution.y;

                float t = u_time * 0.5;

                float wave1 = sin(uv.x * 3.0 + t);
                float wave2 = sin(uv.y * 5.0 - t * 1.2);
                float wave3 = sin((uv.x + uv.y) * 4.0 + t * 0.8);

                float pattern = wave1 + wave2 + wave3;
                pattern = sin(pattern * 2.0);

                float r = 0.5 + 0.5 * sin(pattern + t);
                float g = 0.5 + 0.5 * sin(pattern + t * 1.3);
                float b = 0.5 + 0.5 * sin(pattern + t * 1.7);

                return half4(r, g, b, 1.0);
            }
            """
        )
    }
    @Language("agsl")
    val agslShader = RuntimeShader("""
    uniform float2 resolution;
    uniform float time;

    float f(vec3 p) {
        p.z -= time * 10.0;
        float a = p.z * 0.1;
        float s = sin(a);
        float c = cos(a);
        p.xy = float2(p.x * c - p.y * s, p.x * s + p.y * c);
        return 0.1 - length(cos(p.xy) + sin(p.yz));
    }

    vec4 main(vec2 fragCoord) {
        vec3 d = float3(0.5 - fragCoord / resolution.y, 1.0);
        vec3 p = float3(0.0);
        for (int i = 0; i < 32; i++) {
            p += f(p) * d;
        }
        vec3 col = (sin(p) + float3(2.0, 5.0, 12.0)) / length(p);
        return vec4(col, 1.0);
    }
""".trimIndent()
    )


    val time = remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { nanos ->
                time.value = (nanos / 1_000_000_000f)
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        agslShader.setFloatUniform("time", time.value)
        agslShader.setFloatUniform("resolution", size.width, size.height)
        drawRect(brush = ShaderBrush(agslShader))
    }
}



