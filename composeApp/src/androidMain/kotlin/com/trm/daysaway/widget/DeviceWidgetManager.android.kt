package com.trm.daysaway.widget

import android.content.Context
import android.os.Build
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.trm.daysaway.core.base.util.actionIntent
import com.trm.daysaway.core.domain.Countdown
import com.trm.daysaway.core.domain.WidgetManager
import com.trm.daysaway.widget.countdown.CountdownWidget
import com.trm.daysaway.widget.countdown.CountdownWidgetActions
import com.trm.daysaway.widget.countdown.CountdownWidgetReceiver
import com.trm.daysaway.widget.countdown.CountdownWidgetState
import com.trm.daysaway.widget.countdown.countdownWidgetPinnedCallback
import com.trm.daysaway.widget.countdown.updateCountdownWidgetIntent

actual class DeviceWidgetManager(private val context: Context) : WidgetManager {
  override suspend fun addCountdownWidget(countdown: Countdown) {
    GlanceAppWidgetManager(context)
      .requestPinGlanceAppWidget(
        receiver = CountdownWidgetReceiver::class.java,
        preview = if (Build.VERSION.SDK_INT >= 31) CountdownWidget() else null,
        previewState = CountdownWidgetState.Ready(countdown),
        successCallback = context.countdownWidgetPinnedCallback(countdown),
      )
  }

  override fun updateCountdownWidget(widgetId: Int, countdown: Countdown) {
    context.sendBroadcast(context.updateCountdownWidgetIntent(widgetId, countdown))
  }

  override fun updateAllCountdownWidgets() {
    context.sendBroadcast(
      context.actionIntent<CountdownWidgetReceiver>(CountdownWidgetActions.UPDATE_ALL)
    )
  }
}
