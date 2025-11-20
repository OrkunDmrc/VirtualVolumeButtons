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
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import com.example.virtualvolumebuttons.Actions.VolumeDownAction
import com.example.virtualvolumebuttons.Actions.VolumeUpAction
import com.example.virtualvolumebuttons.R

class ClassicRowButtonsWidget() : GlanceAppWidget() {
    @Composable
    override fun Content(){
        Row (
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(5.dp)
                .background(Color(0x80000000)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                provider = ImageProvider(R.drawable.ic_volume_down),
                contentDescription = "Volume Down",
                modifier = GlanceModifier
                    .size(35.dp)
                    .clickable(onClick = actionRunCallback<VolumeDownAction>())
            )
            Spacer(modifier = GlanceModifier.size(5.dp))
            Image(
                provider = ImageProvider(R.drawable.ic_volume_up),
                contentDescription = "Volume Up",
                modifier = GlanceModifier
                    .size(35.dp)
                    .clickable(onClick = actionRunCallback<VolumeUpAction>())
            )
        }
    }
}

