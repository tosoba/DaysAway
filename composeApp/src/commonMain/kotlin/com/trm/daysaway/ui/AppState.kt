package com.trm.daysaway.ui

import com.trm.daysaway.domain.Countdown

expect class AppState {
  val onCountdownConfirmClick: (Countdown) -> Unit
}
