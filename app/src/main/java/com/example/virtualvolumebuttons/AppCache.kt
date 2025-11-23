package com.example.virtualvolumebuttons

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit
import com.example.virtualvolumebuttons.objects.VolumeButtons

val bubbleRowDpX = 120
val bubbleRowDpY = 70
val bubbleColumnDpX = 70
val bubbleColumnDpY = 120
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