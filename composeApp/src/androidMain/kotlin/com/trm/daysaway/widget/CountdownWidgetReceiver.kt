package com.trm.daysaway.widget

import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.trm.daysaway.core.base.util.updateWidget

class CountdownWidgetReceiver : GlanceAppWidgetReceiver() {
  override val glanceAppWidget = CountdownWidget()

  override fun onReceive(context: Context, intent: Intent) {
    super.onReceive(context, intent)
    when (intent.action) {
      CountdownWidgetActions.UPDATE -> {
        val extras = requireNotNull(intent.extras) { "Extras were not provided to UPDATE action." }
        glanceAppWidget.updateWidget(
          widgetId = extras.getInt(CountdownWidgetExtras.WIDGET_ID),
          definition = CountdownWidgetStateDefinition,
          context = context,
        ) {
          CountdownWidgetState.Ready(extras.toCountdown())
        }
      }
    }
  }
}
