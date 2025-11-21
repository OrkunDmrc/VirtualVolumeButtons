package com.example.virtualvolumebuttons

import android.app.Service
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
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
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

val bubbleDp = 60
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
        val bubbleSize = bubbleDp.dpToPx()
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

@Composable
fun FloatingBubble(onDragStart: () -> Unit, onDrag: (Float, Float) -> Unit, onDragEnd: () -> Unit) {
    Box(
        modifier = Modifier
            .size(bubbleDp.dp)
            .background(Color.Blue, CircleShape)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDragEnd = { onDragEnd() }
                ) { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount.x, dragAmount.y)
                }
            }
    )
}

@Composable
fun DismissBubble(isOverlapping: Boolean) {
    val size by animateDpAsState(if (isOverlapping) dismissDp.dp else bubbleDp.dp)
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
