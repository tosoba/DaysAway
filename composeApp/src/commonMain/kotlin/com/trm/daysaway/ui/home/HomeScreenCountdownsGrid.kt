package com.trm.daysaway.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun HomeScreenCountdownsGrid(
  state: HomeScreenState,
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier,
)
