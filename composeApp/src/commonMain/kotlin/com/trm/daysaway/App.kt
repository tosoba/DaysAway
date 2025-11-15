@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.trm.daysaway

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
  MaterialExpressiveTheme {
    Scaffold {
      CountdownDaysEditor(modifier = Modifier.fillMaxSize().background(Color.White).padding(it))
    }
  }
}
