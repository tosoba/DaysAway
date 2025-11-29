@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.trm.daysaway.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LargeExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(onAddWidgetClick: () -> Unit) {
  Scaffold(
    topBar = { CenterAlignedTopAppBar(title = { Text("DaysAway") }) },
    floatingActionButton = {
      LargeExtendedFloatingActionButton(onClick = onAddWidgetClick) { Text("Add widget") }
    },
  ) { contentPadding ->
    ExistingWidgetsGrid(modifier = Modifier.fillMaxSize().padding(contentPadding))
  }
}
