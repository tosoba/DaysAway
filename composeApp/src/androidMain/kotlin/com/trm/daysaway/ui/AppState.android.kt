package com.trm.daysaway.ui

import androidx.compose.runtime.Stable
import com.trm.daysaway.domain.Countdown

@Stable actual class AppState(actual val onCountdownConfirmClick: (Countdown) -> Unit)
