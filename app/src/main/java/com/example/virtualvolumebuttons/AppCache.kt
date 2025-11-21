package com.example.virtualvolumebuttons

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit

val bubbleRowDpX = 120
val bubbleRowDpY = 70
val bubbleColumnDpX = 70
val bubbleColumnDpY = 120
object AppCache {
    private lateinit var prefs: SharedPreferences


    fun init(context: Context) {
        prefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
    }

    fun saveSelectedColors(bgColor: Color, btnColor: Color, isRow: Boolean) {
        prefs.edit {
            putLong("bgColor", bgColor.value.toLong())
            putLong("btnColor", btnColor.value.toLong())
            putBoolean("isRow", isRow)
        }
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

    fun getBubbleRowDpX(): Int {
        return 100
    }

    fun getBubbleRowDpY(): Int {
        return if(isRow()) 100 else 60
    }


}