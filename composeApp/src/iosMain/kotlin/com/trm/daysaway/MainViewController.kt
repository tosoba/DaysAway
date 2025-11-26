package com.trm.daysaway

import androidx.compose.ui.window.ComposeUIViewController
import com.trm.daysaway.ui.App
import com.trm.daysaway.widget.FakeWidgetManager

fun MainViewController() = ComposeUIViewController { App(FakeWidgetManager()) }
