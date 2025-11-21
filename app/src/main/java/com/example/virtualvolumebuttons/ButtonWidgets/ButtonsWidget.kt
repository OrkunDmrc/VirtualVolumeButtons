package com.example.virtualvolumebuttons.ButtonWidgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.Alignment
import androidx.glance.background
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.layout.size
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import com.example.virtualvolumebuttons.Actions.VolumeDownAction
import com.example.virtualvolumebuttons.Actions.VolumeUpAction
import com.example.virtualvolumebuttons.AppCache
import com.example.virtualvolumebuttons.R

class ButtonsWidget() : GlanceAppWidget() {
    @Composable
    override fun Content(){
        val bg = AppCache.getBgColor()
        val btn = AppCache.getBtnColor()
        val volumeUpId = when(btn) {
            Color.Black -> R.drawable.ic_volume_up_black
            else -> R.drawable.ic_volume_up
        }
        val volumeDownId = when(btn) {
            Color.Black -> R.drawable.ic_volume_down_black
            else -> R.drawable.ic_volume_down
        }
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(5.dp)
                .background(bg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                provider = ImageProvider(volumeUpId),
                contentDescription = "Volume Up",
                modifier = GlanceModifier
                    .size(35.dp)
                    .clickable(onClick = actionRunCallback<VolumeUpAction>())
            )
            Spacer(modifier = GlanceModifier.height(5.dp))
            Image(
                provider = ImageProvider(volumeDownId),
                contentDescription = "Volume Down",
                modifier = GlanceModifier
                    .size(35.dp)
                    .clickable(onClick = actionRunCallback<VolumeDownAction>())
            )
        }
    }
}

