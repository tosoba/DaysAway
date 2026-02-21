package com.trm.daysaway.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.trm.daysaway.core.base.PlatformContext
import com.trm.daysaway.domain.Countdown
import com.trm.daysaway.ui.AppState

actual class HomeScreenState(val countdowns: List<Countdown>) {
  actual fun refresh(context: PlatformContext) = Unit
}

@Composable
actual fun rememberHomeScreenState(appState: AppState) =
  remember(appState.countdowns) { HomeScreenState(appState.countdowns) }
