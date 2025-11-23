package com.example.virtualvolumebuttons

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.virtualvolumebuttons.Components.WidgetBox
import com.example.virtualvolumebuttons.objects.VolumeButtons
import com.example.virtualvolumebuttons.ui.theme.VirtualVolumeButtonsTheme
import androidx.core.net.toUri

val volumeButtonsList : List<VolumeButtons> = listOf(
    VolumeButtons(id = 1, bgColor = Color.Black, btnColor = Color.White, isRow = false),
    VolumeButtons(id = 2, bgColor = Color.Black, btnColor = Color.White, isRow = true),
    VolumeButtons(id = 3, bgColor = Color.Black, btnColor = Color.Red, isRow = false),
    VolumeButtons(id = 4, bgColor = Color.Black, btnColor = Color.Red, isRow = true),
    VolumeButtons(id = 5, bgColor = Color.Black, btnColor = Color.Green, isRow = false),
    VolumeButtons(id = 6, bgColor = Color.Black, btnColor = Color.Green, isRow = true),
    VolumeButtons(id = 7, bgColor = Color.Black, btnColor = Color.Cyan, isRow = false),
    VolumeButtons(id = 8, bgColor = Color.Black, btnColor = Color.Cyan, isRow = true)
)

lateinit var intent: Intent
lateinit var serviceIntent: Intent


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppCache.init(this)
        intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:$packageName".toUri()
        )
        serviceIntent = Intent(this, FloatingService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            startActivity(intent)
        } else {
            startService(serviceIntent)
        }
        setContent {
            VirtualVolumeButtonsTheme {
                MyScreen(this)
            }
        }
    }
}

@Composable
fun MyScreen(context: Context){
    val selectedId = remember { mutableStateOf(AppCache.getId()) }
    Scaffold(
        containerColor = Color(0xFFCCC2DC),
        contentColor = Color(0xFFCCC2DC),
        content = { padding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in volumeButtonsList.indices step 2) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        WidgetBox(volumeButtons = volumeButtonsList[i], selectedId = selectedId.value, onSelected = {
                            selectedId.value = volumeButtonsList[i].id
                            AppCache.saveSelectedButtons(volumeButtonsList[i])
                            context.stopService(serviceIntent)
                            context.startService(serviceIntent)
                        })
                        WidgetBox(volumeButtons = volumeButtonsList[i + 1], selectedId = selectedId.value, onSelected = {
                            selectedId.value = volumeButtonsList[i + 1].id
                            AppCache.saveSelectedButtons(volumeButtonsList[i + 1])
                            context.stopService(serviceIntent)
                            context.startService(serviceIntent)
                        })
                    }
                }
            }
        }
    )
}
