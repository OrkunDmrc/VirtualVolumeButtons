package com.example.virtualvolumebuttons.Components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.virtualvolumebuttons.AppCache
import com.example.virtualvolumebuttons.bubbleColumnDpX
import com.example.virtualvolumebuttons.bubbleColumnDpY
import com.example.virtualvolumebuttons.bubbleRowDpX
import com.example.virtualvolumebuttons.bubbleRowDpY

@Composable
fun FloatingBubble(
    context: Context,
    onDragStart: () -> Unit,
    onDrag: (Float, Float) -> Unit,
    onDragEnd: () -> Unit) {
    val width = if(AppCache.isRow()) bubbleRowDpX else bubbleColumnDpX
    val height = if(AppCache.isRow()) bubbleRowDpY else bubbleColumnDpY
    Box(
        modifier = Modifier
            .size(width = width.dp, height = height.dp)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDragEnd = { onDragEnd() }
                ) { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount.x, dragAmount.y)
                }
            },
        contentAlignment = Alignment.Center,
        ) {
        WidgetCom(context)
    }
}