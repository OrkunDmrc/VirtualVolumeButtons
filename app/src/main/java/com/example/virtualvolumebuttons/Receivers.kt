package com.example.virtualvolumebuttons

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.example.virtualvolumebuttons.ButtonWidgets.ClassicButtonsWidget
import com.example.virtualvolumebuttons.ButtonWidgets.ClassicRowButtonsWidget
import com.example.virtualvolumebuttons.ButtonWidgets.ReversedButtonsWidget
import com.example.virtualvolumebuttons.ButtonWidgets.ReversedRowButtonsWidget

class Receivers {
}

class ClassicButtonsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ClassicButtonsWidget()
}

class ReversedButtonsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ReversedButtonsWidget()
}

class ClassicRowButtonsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ClassicRowButtonsWidget()
}

class ReversedRowButtonsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ReversedRowButtonsWidget()
}