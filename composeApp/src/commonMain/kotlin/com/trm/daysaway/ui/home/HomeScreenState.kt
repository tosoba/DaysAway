package com.trm.daysaway.ui.home

import androidx.compose.runtime.Composable
import com.trm.daysaway.core.base.PlatformContext

expect class HomeScreenState {
  fun refresh(context: PlatformContext)
}

@Composable expect fun rememberHomeScreenState(vararg inputs: Any?): HomeScreenState
