package com.example.virtualvolumebuttons.Components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.virtualvolumebuttons.dismissDp

@Composable
fun DismissBubble(isOverlapping: Boolean) {
    val size by animateDpAsState(if (isOverlapping) dismissDp.dp else 60.dp)
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(if (isOverlapping) Color.Red.copy(alpha = 0.7f) else Color.Gray.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.White)
    }
}