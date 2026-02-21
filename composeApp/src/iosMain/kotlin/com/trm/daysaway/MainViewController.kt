package com.trm.daysaway

import androidx.compose.ui.window.ComposeUIViewController
import com.trm.daysaway.domain.Countdown
import com.trm.daysaway.ui.App

fun mainViewController(onCountdownConfirmClick: (Countdown) -> Unit) = ComposeUIViewController {
  App(onCountdownConfirmClick = onCountdownConfirmClick)
}
