package com.trm.daysaway.core.base

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

actual typealias PlatformContext = Context

@Composable
actual fun platformContext(): PlatformContext = LocalContext.current