package com.example.shaders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview(showBackground = true)
fun HapticButtonSample(){
    val haptic= LocalHapticFeedback.current
    Box(
        modifier = Modifier.size(150.dp).clickable(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        )
    ){
        Text(
            text = "Tap me for haptics"
        )
    }
}