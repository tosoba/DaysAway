package com.trm.daysaway.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

actual class HomeScreenState

@Composable
actual fun rememberHomeScreenState(vararg inputs: Any?) = remember(inputs) { HomeScreenState() }
