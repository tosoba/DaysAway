package com.trm.daysaway.widget.countdown

import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.trm.daysaway.core.base.util.updateAllWidgets
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
      CountdownWidgetActions.REFRESH -> {
        val extras = requireNotNull(intent.extras) { "Extras were not provided to REFRESH action." }
        glanceAppWidget.updateWidget(
          widgetId = extras.getInt(CountdownWidgetExtras.WIDGET_ID),
          definition = CountdownWidgetStateDefinition,
          context = context,
          updateState = ::refreshWidget,
        )
      }
      CountdownWidgetActions.REFRESH_ALL -> {
        glanceAppWidget.updateAllWidgets(
          definition = CountdownWidgetStateDefinition,
          context = context,
          updateState = ::refreshWidget,
        )
      }
    }
  }

  private fun refreshWidget(state: CountdownWidgetState): CountdownWidgetState =
    when (state) {
      CountdownWidgetState.Empty,
      CountdownWidgetState.Loading -> state
      is CountdownWidgetState.Ready -> state.copy(version = state.version + 1)
    }
}
