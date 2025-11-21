package com.example.virtualvolumebuttons

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.glance.layout.height
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.virtualvolumebuttons.Actions.VolumeDownAction
import com.example.virtualvolumebuttons.Actions.VolumeUpAction
import com.example.virtualvolumebuttons.Components.DismissBubble
import com.example.virtualvolumebuttons.Components.FloatingBubble

val bubbleDpX = 60
val bubbleDpY = 100
val dismissDp = 80
class FloatingService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var bubbleView: ComposeView
    private lateinit var dismissView: ComposeView
    private lateinit var lifecycleOwner: ServiceLifecycleOwner
    private lateinit var bubbleParams: WindowManager.LayoutParams
    private lateinit var dismissParams: WindowManager.LayoutParams
    private var isBubbleOverlapping by mutableStateOf(false)

    override fun onCreate() {
        AppCache.init(this)
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        lifecycleOwner = ServiceLifecycleOwner()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleStart()

        bubbleParams = createLayoutParams()
        dismissParams = createLayoutParams(y = 5.dpToPx(), gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
        dismissView = createDismissView()
        bubbleView = createBubbleView()

        // Position dismiss view closer to the bottom
        windowManager.addView(dismissView, dismissParams)
        windowManager.addView(bubbleView, bubbleParams)
        dismissParams.x = (Resources.getSystem().displayMetrics.widthPixels - dismissDp.dpToPx()) / 2
        dismissParams.y = dismissDp.dpToPx()
    }

    private fun createBubbleView(): ComposeView {
        return ComposeView(this).apply {
            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeViewModelStoreOwner(lifecycleOwner)
            setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            setContent {
                FloatingBubble(
                    context,
                    onDragStart = { dismissView.visibility = View.VISIBLE },
                    onDrag = { dx, dy ->
                        bubbleParams.x += dx.toInt()
                        bubbleParams.y += dy.toInt()
                        windowManager.updateViewLayout(this, bubbleParams)
                        isBubbleOverlapping = checkOverlap()
                    },
                    onDragEnd = {
                        if (checkOverlap()) {
                            stopSelf()
                        }
                        dismissView.visibility = View.GONE
                    }
                )
            }
        }
    }

    private fun createDismissView(): ComposeView {
        return ComposeView(this).apply {
            visibility = View.GONE
            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeViewModelStoreOwner(lifecycleOwner)
            setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            setContent {
                DismissBubble(isOverlapping = isBubbleOverlapping)
            }
        }
    }

    private fun checkOverlap(): Boolean {
        val bubbleSize = bubbleDpX.dpToPx()
        val dismissSize = dismissDp.dpToPx()

        val bubbleCenterX = bubbleParams.x + bubbleSize / 2
        val bubbleCenterY = bubbleParams.y + bubbleSize / 2

        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val dismissLeft = dismissParams.x
        val dismissRight = dismissParams.x + dismissSize
        val dismissBottom = screenHeight - dismissParams.y
        val dismissTop = dismissBottom - dismissSize

        return bubbleCenterX in dismissLeft..dismissRight &&
                bubbleCenterY in dismissTop..dismissBottom
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleOwner.handleStop()
        windowManager.removeView(bubbleView)
        windowManager.removeView(dismissView)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}
private fun createLayoutParams(
    x: Int = 0, y: Int = 0,
    gravity: Int = Gravity.TOP or Gravity.START
) = WindowManager.LayoutParams(
    WindowManager.LayoutParams.WRAP_CONTENT,
    WindowManager.LayoutParams.WRAP_CONTENT,
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else @Suppress(
        "DEPRECATION"
    ) WindowManager.LayoutParams.TYPE_PHONE,
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
    PixelFormat.TRANSLUCENT
).apply {
    this.gravity = gravity
    this.x = x
    this.y = y
}

private class ServiceLifecycleOwner :
    LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    override val viewModelStore = ViewModelStore()
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    fun handleStart() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    fun handleStop() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    fun performRestore(bundle: Bundle?) {
        savedStateRegistryController.performRestore(bundle)
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry
}
