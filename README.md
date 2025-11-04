# 🎨 Jetpack Compose Shaders Practice (AGSL)

Shaders — *“programs that run on the GPU”* — are among the most powerful tools available for building visually captivating and efficient graphics.
This repository documents my journey of exploring and practicing **Android Graphics Shading Language (AGSL)** within **Jetpack Compose**, covering everything from basic gradient creation to advanced animated visual effects.

---

## **🚀 Overview**

Shaders are small programs that execute directly on the **GPU (Graphics Processing Unit)**, making them **highly efficient** in rendering complex visual effects without burdening the CPU.
This efficiency makes shaders ideal for use in **real-time animations**, **custom backgrounds**, and **interactive graphics** — all while maintaining smooth app performance.

In this repository, I experimented with:

* Basic and animated **color gradients**
* **Procedural visual patterns** and **dynamic lighting effects**
* Realistic **rain**, **snow**, and **ripple** simulations
* **Curved and expressive backgrounds** using AGSL

---

## **💡 Using Runtime Shaders in Jetpack Compose**

There are **two main ways** to integrate and apply shaders in **Jetpack Compose**:

### 1️⃣ **Shader Brush**

You can use a shader as a **Brush**, which allows it to be applied as a background or fill for any composable.
This approach is ideal for **dynamic gradient backgrounds**, **animated surfaces**, or **decorative UI elements**.

```kotlin
val shader = RuntimeShader(shaderCode)
val brush = ShaderBrush(shader)
Box(
    modifier = Modifier
        .fillMaxSize()
        .background(brush)
)
```

### 2️⃣ **Render Effect**

You can also use shaders as **RenderEffects**, applying them via the `graphicsLayer` modifier.
This approach gives you fine-grained control over how the shader interacts with composable content and layers.

```kotlin
val shader = RuntimeShader(shaderCode)
val renderEffect = RenderEffect.createRuntimeShaderEffect(shader, "image")
Box(
    modifier = Modifier
        .fillMaxSize()
        .graphicsLayer {
            renderEffect?.let { this.renderEffect = it }
        }
)
```

---

## **⚠️ Limitation**

> **Minimum API Level:** 33 (Android 13, Tiramisu)
>
> AGSL shaders and the `RuntimeShader` & `RenderEffect` APIs are **only available on Android 13 or higher**.
> For devices running older versions, these effects will not render or may crash if not handled conditionally.

---

## **🎥 Demo Video**

> 🎬 Watch the full demo to see all shader effects in action!


```

```

---

## **🧩 Demonstrated Shader Effects**

Here are the shader effects showcased in the demo, along with their file locations in the project:

| Effect Name             | Description                                                            | File Path                       |
| ----------------------- | ---------------------------------------------------------------------- | ------------------------------- |
| 🌧️ **Rain Effect**     | Animated rain simulation using vertical particle streaks               | `kinto/RainSample.kt`           |
| 💧 **Ripple Effect**    | Expanding ripple animation inspired by water surface physics           | `kinto/RippleEffectSample.kt`   |
| ❄️ **Snow Effect**      | Floating and falling snowflakes rendered using time-based animation    | `romain/Sample4.kt`             |
| 🌈 **Curvy Background** | Dynamic background with smooth curvy patterns and gradient transitions | `timo/SimpleAGSLBackgrounds.kt` |

---

## **🧠 What I Learned**

* Understanding **AGSL (Android Graphics Shading Language)** fundamentals
* Writing and structuring shader programs for use in Compose
* Using **`RuntimeShader`** and **`RenderEffect`** APIs effectively
* Creating **dynamic gradients**, **procedural textures**, and **animated effects**
* Balancing GPU rendering performance and UI responsiveness
* Applying shaders both as **background brushes** and **render effects**

---

## **🛠️ Tech Stack**

* **Language:** Kotlin
* **Framework:** Jetpack Compose
* **Shader Language:** Android Graphics Shading Language (AGSL)
* **Graphics APIs:** RuntimeShader, RenderEffect
* **Minimum Android Version:** API 33 (Tiramisu)

---

## **🌟 Support**

If you found this project useful, **please give it a ⭐** — your support inspires more shader experiments in Jetpack Compose.

---
