package com.example.virtualvolumebuttons.Components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.virtualvolumebuttons.Actions.VolumeDownAction
import com.example.virtualvolumebuttons.Actions.VolumeUpAction
import com.example.virtualvolumebuttons.AppCache
import com.example.virtualvolumebuttons.R
import com.example.virtualvolumebuttons.bubbleDpX
import com.example.virtualvolumebuttons.bubbleDpY

@Composable
fun WidgetCom(context: Context?, bgColor: Color?, btnColor: Color?){
    val bg = bgColor ?: AppCache.getBgColor()
    val btn = btnColor ?: AppCache.getBtnColor()
    val isRow = AppCache.isRow()
    val round = 20.dp
    val size = 40.dp
    val space = 5.dp
    if (isRow) {
        Row(
            modifier = Modifier
                .width(bubbleDpX.dp)
                .height(bubbleDpY.dp)
                .clip(RoundedCornerShape(round))
                .background(bg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_volume_down),
                contentDescription = "volume down",
                colorFilter = ColorFilter.tint(btn),
                modifier = Modifier
                    .size(size)
                    .clickable{
                        if(context != null)
                            VolumeDownAction().volumeDown(context)
                        else
                            null
                    }
            )
            Spacer(modifier = Modifier.width(space))
            Image(
                painter = painterResource(R.drawable.ic_volume_up),
                contentDescription = "volume up",
                colorFilter = ColorFilter.tint(btn),
                modifier = Modifier
                    .size(size)
                    .clickable{
                        if(context != null)
                            VolumeUpAction().volumeUp(context)
                        else
                            null
                    }
            )
        }
    } else {
        Column(
            modifier = Modifier
                .width(bubbleDpX.dp)
                .height(bubbleDpY.dp)
                .clip(RoundedCornerShape(round))
                .background(bg),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_volume_up),
                contentDescription = "volume up",
                colorFilter = ColorFilter.tint(btn),
                modifier = Modifier
                    .size(size)
                    .clickable{
                        if(context != null)
                            VolumeUpAction().volumeUp(context)
                        else
                            null
                    }
            )
            Spacer(modifier = Modifier.height(space))
            Image(
                painter = painterResource(R.drawable.ic_volume_down),
                contentDescription = "volume down",
                colorFilter = ColorFilter.tint(btn),
                modifier = Modifier
                    .size(size)
                    .clickable{
                        if(context != null)
                            VolumeDownAction().volumeDown(context)
                        else
                            null
                    }
            )
        }
    }


}