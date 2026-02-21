package com.trm.daysaway.ui

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.trm.daysaway.domain.Countdown

@Stable
actual class AppState(actual val onCountdownConfirmClick: (Countdown) -> Unit) {
  var countdowns by mutableStateOf(emptyList<Countdown>())
}
