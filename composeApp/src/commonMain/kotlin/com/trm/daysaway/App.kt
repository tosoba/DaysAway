@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.trm.daysaway

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
  MaterialExpressiveTheme { CountdownDaysEditor() }
}
