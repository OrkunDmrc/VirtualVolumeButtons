package com.example.virtualvolumebuttons.objects

import androidx.compose.ui.graphics.Color

data class VolumeButtons(
    val id: Int,
    val bgColor: Color,
    val btnColor: Color,
    val isRow: Boolean
)