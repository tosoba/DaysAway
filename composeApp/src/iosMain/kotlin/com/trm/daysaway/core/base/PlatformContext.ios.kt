package com.trm.daysaway.core.base

import androidx.compose.runtime.Composable

actual abstract class PlatformContext private constructor() {
  companion object {
    val INSTANCE = object : PlatformContext() {}
  }
}

@Composable actual fun platformContext(): PlatformContext = PlatformContext.INSTANCE
