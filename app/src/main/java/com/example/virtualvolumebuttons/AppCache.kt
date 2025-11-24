package com.example.virtualvolumebuttons

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit
import com.example.virtualvolumebuttons.objects.VolumeButtons

val bubbleRowDpX = 95
val bubbleRowDpY = 55
val bubbleColumnDpX = 55
val bubbleColumnDpY = 95

val primaryColor = Color(0xFF01579B)
val secondaryColor = Color(0xFFF6C810)
val whiteColor = Color(0xFFF5F5F5)
val blackColor = Color(0xFF050505)

object AppCache {
    private lateinit var prefs: SharedPreferences


    fun init(context: Context) {
        prefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
    }

    fun saveSelectedButtons(volumeButtons: VolumeButtons) {
        prefs.edit {
            putInt("selectedId", volumeButtons.id)
            putLong("bgColor", volumeButtons.bgColor.value.toLong())
            putLong("btnColor", volumeButtons.btnColor.value.toLong())
            putBoolean("isRow", volumeButtons.isRow)
        }
    }

    fun getId(): Int {
        return prefs.getInt("selectedId", 1)
    }

    fun getBgColor(): Color {
        val saved = prefs.getLong(
            "bgColor",
            Color.Black.value.toLong()
        )
        return Color(saved.toULong())
    }

    fun getBtnColor(): Color {
        val saved = prefs.getLong(
            "btnColor",
            Color.White.value.toLong()
        )
        return Color(saved.toULong())
    }

    fun isRow(): Boolean {
        return prefs.getBoolean("isRow", false)
    }




}