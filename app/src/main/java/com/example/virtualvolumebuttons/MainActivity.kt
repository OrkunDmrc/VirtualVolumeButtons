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
import com.google.android.gms.ads.MobileAds

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
        },
        bottomBar = {
            Column {
                AndroidView(modifier = Modifier.fillMaxWidth(), factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = "ca-app-pub-3940256099942544/9214589741"
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
