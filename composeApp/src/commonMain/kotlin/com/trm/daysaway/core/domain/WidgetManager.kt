package com.trm.daysaway.core.domain

interface WidgetManager {
  suspend fun addCountdownWidget(countdown: Countdown)
}
