package com.example.shaders.rinkin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true)
fun SampleBox(){
    var size by remember { mutableStateOf(Size.Zero) }
    Box(
        modifier = Modifier.fillMaxSize().onSizeChanged{
            size=Size(it.width.toFloat(),it.height.toFloat())
        },
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "width ${size.width},height ${size.height} pixels ${size.width*size.height}"
        )
    }
}