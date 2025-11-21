@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.trm.daysaway.ui

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.runtime.Composable
import com.trm.daysaway.ui.widget.WidgetScreen

@Composable
fun App() {
  MaterialExpressiveTheme { WidgetScreen() }
}
