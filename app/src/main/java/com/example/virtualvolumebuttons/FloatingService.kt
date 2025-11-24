package com.example.virtualvolumebuttons

import android.app.Service
import android.content.Intent
import android.content.res.Resources
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
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
import com.example.virtualvolumebuttons.Components.DismissBubble
import com.example.virtualvolumebuttons.Components.FloatingBubble
import com.google.android.gms.ads.AdSize

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

        val bubbleXWidth = if(AppCache.isRow()) bubbleRowDpX else bubbleColumnDpX
        val startX = (Resources.getSystem().displayMetrics.widthPixels - bubbleXWidth.dpToPx()) / 2
        bubbleParams = createLayoutParams(x = startX, y = AdSize.BANNER.height.dpToPx())
        dismissParams = createLayoutParams(y = 5.dpToPx(), gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
        dismissView = createDismissView()
        bubbleView = createBubbleView()
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
                            bubbleView.visibility = View.GONE
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
        val bubbleSizeX = if(AppCache.isRow()) bubbleRowDpX else bubbleColumnDpX
        val bubbleSizeY = if(AppCache.isRow()) bubbleRowDpY else bubbleColumnDpY
        val dismissSize = dismissDp.dpToPx()

        val bubbleCenterX = bubbleParams.x + bubbleSizeX / 2
        val bubbleCenterY = bubbleParams.y + bubbleSizeY / 2

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

    fun refreshFloatingService() {
        val intent = Intent(this, FloatingService::class.java)
        stopService(intent)
        startService(intent)
    }

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
