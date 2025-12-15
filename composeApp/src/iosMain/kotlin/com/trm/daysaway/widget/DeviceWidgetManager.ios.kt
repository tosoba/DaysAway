package com.trm.daysaway.widget

import com.trm.daysaway.domain.Countdown
import com.trm.daysaway.domain.WidgetManager

actual class DeviceWidgetManager : WidgetManager {
  override suspend fun addCountdownWidget(countdown: Countdown) {}

  override fun updateCountdownWidget(widgetId: Int, countdown: Countdown) {}

  override fun refreshAllCountdownWidgets() {}
}
