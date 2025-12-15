package com.trm.daysaway.widget.countdown

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent

class CountdownWidget : GlanceAppWidget() {
  override val sizeMode: SizeMode = SizeMode.Exact

  override val stateDefinition = CountdownWidgetStateDefinition

  override suspend fun provideGlance(context: Context, id: GlanceId) {
    provideContent { CountdownWidgetContent(id) }
  }
}
