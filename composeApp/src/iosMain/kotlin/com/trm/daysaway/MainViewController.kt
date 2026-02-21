package com.trm.daysaway

import androidx.compose.ui.window.ComposeUIViewController
import com.trm.daysaway.ui.App
import com.trm.daysaway.ui.AppState

fun mainViewController(state: AppState) = ComposeUIViewController { App(state = state) }
