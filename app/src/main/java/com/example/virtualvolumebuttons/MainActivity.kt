package com.example.virtualvolumebuttons

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.glance.layout.height
import com.example.virtualvolumebuttons.Components.WidgetCom
import com.example.virtualvolumebuttons.ui.theme.VirtualVolumeButtonsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppCache.init(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        } else {
            startService(Intent(this, FloatingService::class.java))
        }
        setContent {
            VirtualVolumeButtonsTheme {
                Scaffold(
                    content = { padding ->
                        Column (
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Virtual Volume Buttons",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(){
                                WidgetCom(isRow = false)
                                WidgetCom(isRow = true)
                                WidgetCom(bgColor = Color.Black, btnColor = Color.Red, isRow = false)
                                WidgetCom(bgColor = Color.Black, btnColor = Color.Red, isRow = true)
                            }
                            Row(){
                                WidgetCom(bgColor = Color.Black, btnColor = Color.Green, isRow = false)
                                WidgetCom(bgColor = Color.Black, btnColor = Color.Green, isRow = true)
                                WidgetCom(bgColor = Color.Black, btnColor = Color.Cyan, isRow = false)
                                WidgetCom(bgColor = Color.Black, btnColor = Color.Cyan, isRow = true)
                            }
                        }

                    }
                )
            }
        }
    }
}
