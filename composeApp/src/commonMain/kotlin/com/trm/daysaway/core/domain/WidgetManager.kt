package com.trm.daysaway.core.domain

interface WidgetManager {
  suspend fun addCountdownWidget(countdown: Countdown)

  fun updateCountdownWidget(widgetId: Int, countdown: Countdown)
}
