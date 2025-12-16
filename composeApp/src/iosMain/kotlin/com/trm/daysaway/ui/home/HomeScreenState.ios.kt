package com.trm.daysaway.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.trm.daysaway.core.base.PlatformContext

actual class HomeScreenState {
  actual fun refresh(context: PlatformContext) = Unit
}

@Composable
actual fun rememberHomeScreenState(vararg inputs: Any?) = remember(inputs) { HomeScreenState() }
