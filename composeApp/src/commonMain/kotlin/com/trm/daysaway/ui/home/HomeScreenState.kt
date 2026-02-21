package com.trm.daysaway.ui.home

import androidx.compose.runtime.Composable
import com.trm.daysaway.core.base.PlatformContext
import com.trm.daysaway.ui.AppState

expect class HomeScreenState {
  fun refresh(context: PlatformContext)
}

@Composable expect fun rememberHomeScreenState(appState: AppState): HomeScreenState
