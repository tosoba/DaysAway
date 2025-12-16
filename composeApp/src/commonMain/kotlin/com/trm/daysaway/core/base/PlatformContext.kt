package com.trm.daysaway.core.base

import androidx.compose.runtime.Composable

expect abstract class PlatformContext

@Composable
expect fun platformContext(): PlatformContext
