package com.trm.daysaway.ui.home

import androidx.compose.runtime.Composable

expect class HomeScreenState

@Composable
expect fun rememberHomeScreenState(vararg inputs: Any?): HomeScreenState
