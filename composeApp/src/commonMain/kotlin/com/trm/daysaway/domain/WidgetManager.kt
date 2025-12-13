package com.trm.daysaway.domain

interface WidgetManager {
  suspend fun addCountdownWidget(countdown: Countdown)

  fun updateCountdownWidget(widgetId: Int, countdown: Countdown)

  fun updateAllCountdownWidgets()
}
