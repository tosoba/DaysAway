package com.trm.daysaway.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
expect fun HomeScreenCountdowns(
  state: HomeScreenState,
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier,
)

@Composable
fun HomeScreenCountdownsLazyVerticalGrid(
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier,
  content: LazyGridScope.() -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 150.dp),
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = contentPadding,
    content = content,
  )
}
