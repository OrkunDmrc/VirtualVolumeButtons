package com.example.virtualvolumebuttons.Components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.virtualvolumebuttons.Actions.VolumeDownAction
import com.example.virtualvolumebuttons.Actions.VolumeUpAction
import com.example.virtualvolumebuttons.AppCache
import com.example.virtualvolumebuttons.R
import com.example.virtualvolumebuttons.bubbleColumnDpX
import com.example.virtualvolumebuttons.bubbleColumnDpY
import com.example.virtualvolumebuttons.bubbleRowDpX
import com.example.virtualvolumebuttons.bubbleRowDpY
import com.example.virtualvolumebuttons.objects.VolumeButtons

@Composable
fun WidgetCom(
        context: Context? = null,
        volumeButtons: VolumeButtons? = null
    ){
    val bg = volumeButtons?.bgColor ?: AppCache.getBgColor()
    val btn = volumeButtons?.btnColor ?: AppCache.getBtnColor()
    val isRow = volumeButtons?.isRow?: AppCache.isRow()
    val round = 20.dp
    val size = 35.dp
    val space = 10.dp
    val clickableUpModifier =
        if (context != null) {
            Modifier.clickable {
                VolumeUpAction().volumeUp(context)
            }
        } else {
            Modifier
        }
    val clickableDownModifier =
        if (context != null) {
            Modifier.clickable {
                VolumeDownAction().volumeDown(context)
            }
        } else {
            Modifier
        }
    if (isRow) {
        Row(
            modifier = Modifier
                .width(bubbleRowDpX.dp)
                .height(bubbleRowDpY.dp)
                .clip(RoundedCornerShape(round))
                .background(bg.copy(alpha = 0.5f))
                .border(
                    width = 1.dp,
                    color = btn,
                    shape = RoundedCornerShape(round)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(round),
                        clip = true
                    )
                    .background(bg)
            ){
                Image(
                    painter = painterResource(R.drawable.ic_volume_down),
                    contentDescription = "volume down",
                    colorFilter = ColorFilter.tint(btn),
                    modifier = Modifier
                        .size(size)
                        .then(clickableDownModifier)
                        .border(
                            width = 1.dp,
                            color = btn,
                            shape = CircleShape
                        )
                )
            }
            Spacer(modifier = Modifier.width(space))
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(round),
                        clip = true
                    )
                    .background(bg)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_volume_up),
                    contentDescription = "volume up",
                    colorFilter = ColorFilter.tint(btn),
                    modifier = Modifier
                        .size(size)
                        .then(clickableUpModifier)
                        .border(
                            width = 1.dp,
                            color = btn,
                            shape = CircleShape
                        )
                        .padding(2.dp)
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .width(bubbleColumnDpX.dp)
                .height(bubbleColumnDpY.dp)
                .clip(RoundedCornerShape(round))
                .background(bg.copy(alpha = 0.5f))
                .border(
                    width = 1.dp,
                    color = btn,
                    shape = RoundedCornerShape(round)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(round),
                        clip = true
                    )
                    .background(bg)
            ) {
                Image(
                    painter = painterResource(R.drawable.outline_add_24),
                    contentDescription = "volume up",
                    colorFilter = ColorFilter.tint(btn),
                    modifier = Modifier
                        .size(size)
                        .then(clickableUpModifier)
                        .border(
                            width = 1.dp,
                            color = btn,
                            shape = RoundedCornerShape(round)
                        ),
                )
            }
            Spacer(modifier = Modifier.height(space))
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(round),
                        clip = true
                    )
                    .background(bg)
            ) {
                Image(
                    painter = painterResource(R.drawable.remove_24dp),
                    contentDescription = "volume down",
                    colorFilter = ColorFilter.tint(btn),
                    modifier = Modifier
                        .size(size)
                        .then(clickableDownModifier)
                        .border(
                            width = 1.dp,
                            color = btn,
                            shape = RoundedCornerShape(round)
                        ),
                )
            }
        }
    }
}