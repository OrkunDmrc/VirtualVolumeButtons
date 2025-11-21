package com.example.virtualvolumebuttons

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.example.virtualvolumebuttons.ButtonWidgets.ButtonsWidget
import com.example.virtualvolumebuttons.ButtonWidgets.ClassicRowButtonsWidget
import com.example.virtualvolumebuttons.ButtonWidgets.ReversedButtonsWidget
import com.example.virtualvolumebuttons.ButtonWidgets.ReversedRowButtonsWidget

class Receivers {
}

class ButtonsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ButtonsWidget()
}

/*class ReversedButtonsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ReversedButtonsWidget()
}

class ClassicRowButtonsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ClassicRowButtonsWidget()
}

class ReversedRowButtonsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ReversedRowButtonsWidget()
}*/