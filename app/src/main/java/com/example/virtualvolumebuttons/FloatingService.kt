package com.example.virtualvolumebuttons

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp

class FloatingService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var composeView: ComposeView

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Create Compose view for floating bubble
        composeView = ComposeView(this).apply {
            setContent {
                FloatingBubble {
                    stopSelf() // close bubble when clicked
                }
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 100
        params.y = 200

        windowManager.addView(composeView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(composeView)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

@Composable
fun FloatingBubble(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(Color.Blue, CircleShape)
            .clickable { onClick() },
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text("Hi", color = Color.White)
    }
}

