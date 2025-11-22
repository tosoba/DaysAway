package com.trm.daysaway.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.trm.daysaway.core.base.util.getLastWidgetId

class CountdownWidgetPinnedReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    val extras =
      requireNotNull(intent.extras) { "Extras were not provided to CountdownWidgetPinnedReceiver." }
    context.sendBroadcast(
      context.updateCountdownWidgetIntent(
        widgetId =
          context.getLastWidgetId<CountdownWidgetReceiver>()
            ?: throw IllegalArgumentException("Did not find any widget ids for CountdownWidget."),
        countdown = extras.toCountdown(),
      )
    )
  }
}
