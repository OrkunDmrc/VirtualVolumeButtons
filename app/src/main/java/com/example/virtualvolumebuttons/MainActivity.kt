package com.example.virtualvolumebuttons

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.virtualvolumebuttons.Components.WidgetBox
import com.example.virtualvolumebuttons.objects.VolumeButtons
import com.example.virtualvolumebuttons.ui.theme.VirtualVolumeButtonsTheme
import androidx.core.net.toUri
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

val volumeButtonsList : List<VolumeButtons> = listOf(
    VolumeButtons(id = 1, bgColor = Color.Black, btnColor = Color.White, isRow = false),
    VolumeButtons(id = 2, bgColor = Color.Black, btnColor = Color.White, isRow = true),
    VolumeButtons(id = 3, bgColor = Color.White, btnColor = Color.Black, isRow = false),
    VolumeButtons(id = 4, bgColor = Color.White, btnColor = Color.Black, isRow = true),
    VolumeButtons(id = 5, bgColor = Color.Black, btnColor = Color.Red, isRow = false),
    VolumeButtons(id = 6, bgColor = Color.Black, btnColor = Color.Red, isRow = true),
    VolumeButtons(id = 7, bgColor = Color.Black, btnColor = Color.Green, isRow = false),
    VolumeButtons(id = 8, bgColor = Color.Black, btnColor = Color.Green, isRow = true)
)
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
        /*MobileAds.initialize(this){
            Log.d("TAG", "onCreate: initAd")
        }*/
        setContent {
            VirtualVolumeButtonsTheme {
                MyScreen(this)
            }
        }
    }

}

fun getStatusBarHeightDp(context: Context): Int {
    var result = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return (result / context.resources.displayMetrics.density).toInt()
}

@Composable
fun MyScreen(context: Context){
    val selectedId = remember { mutableStateOf(AppCache.getId()) }
    Scaffold(
        containerColor = primaryColor,
        contentColor = primaryColor,
        modifier = Modifier.padding(top = getStatusBarHeightDp(context).dp),
        topBar = {
            Column(
                Modifier.fillMaxWidth()
                    .height(AdSize.BANNER.height.dp),
                    //.background(Color(0xFFC52B2F)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text ="Virtual Volume Buttons",
                    color = whiteColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Divider(
                    color = secondaryColor,
                    thickness = 1.dp
                )
            }
        },
        content = { padding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(bottom = 10.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Select the Buttons", color = whiteColor)
                for (i in volumeButtonsList.indices step 2) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        WidgetBox(volumeButtons = volumeButtonsList[i], selectedId = selectedId.value, onSelected = {
                            selectedId.value = volumeButtonsList[i].id
                            AppCache.saveSelectedButtons(volumeButtonsList[i])
                            context.stopService(serviceIntent)
                            context.startService(serviceIntent)
                        })
                        Spacer(modifier = Modifier.width((bubbleColumnDpX + 4).dp))
                        WidgetBox(volumeButtons = volumeButtonsList[i + 1], selectedId = selectedId.value, onSelected = {
                            selectedId.value = volumeButtonsList[i + 1].id
                            AppCache.saveSelectedButtons(volumeButtonsList[i + 1])
                            context.stopService(serviceIntent)
                            context.startService(serviceIntent)
                        })
                    }
                }
            }
        },
        bottomBar = {
            Column(
                Modifier.fillMaxWidth().height(AdSize.BANNER.height.dp),
            ) {
                AndroidView(modifier = Modifier.fillMaxWidth(), factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = context.getString(R.string.BANNER_ID)
                        loadAd(AdRequest.Builder().build())
                        this.adListener = object : AdListener() {
                            override fun onAdClicked() {
                                Log.d("TAG", "onAdClicked: ")
                            }
                            override fun onAdClosed() {
                                Log.d("TAG", "onAdClosed: ")
                            }
                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Log.d("TAG", "onAdFailedToLoad: $adError")
                            }
                            override fun onAdImpression() {
                            }
                            override fun onAdLoaded() {
                                Log.d("TAG", "onAdLoaded: ")
                            }
                            override fun onAdOpened() {
                                Log.d("TAG", "onAdOpened: ")
                            }
                        }
                    }
                })
            }
        }
    )
}
