package com.trm.daysaway.widget

import com.trm.daysaway.core.domain.Countdown
import com.trm.daysaway.core.domain.WidgetManager

actual class DeviceWidgetManager : WidgetManager {
  override suspend fun addCountdownWidget(countdown: Countdown) {}
}
