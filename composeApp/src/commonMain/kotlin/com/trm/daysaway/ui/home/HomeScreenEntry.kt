package com.trm.daysaway.ui.home

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.trm.daysaway.ui.Home

@Composable
fun EntryProviderScope<NavKey>.HomeScreenEntry(
  state: HomeScreenState,
  onAddWidgetClick: () -> Unit,
) {
  entry<Home> { HomeScreen(state = state, onAddWidgetClick = onAddWidgetClick) }
}
