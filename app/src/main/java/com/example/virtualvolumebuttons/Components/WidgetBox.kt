package com.example.virtualvolumebuttons.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.virtualvolumebuttons.objects.VolumeButtons

@Composable
fun WidgetBox(
    volumeButtons: VolumeButtons,
    selectedId: Int,
    onSelected: () -> Unit
) {
    Box(modifier = Modifier
        .border(
            width = 2.dp,
            color = Color(0xFF6650a4),
            shape = RoundedCornerShape(10.dp)
        )
        .width(120.dp)
        .height(120.dp)
        .padding(5.dp)
        .background(if(selectedId == volumeButtons.id) Color(0xFF6650a4) else Color.Transparent)
        .clickable{onSelected()},
        contentAlignment = Alignment.Center,
    ){
        WidgetCom(volumeButtons = volumeButtons)
    }
}