package com.trm.daysaway.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun HomeScreenCountdowns(
  state: HomeScreenState,
  contentPadding: PaddingValues,
  modifier: Modifier,
) {
  Crossfade(state.countdowns.isEmpty()) { isEmpty ->
    if (isEmpty) {
      HomeScreenNoWidgetsText(
        modifier = modifier.verticalScroll(rememberScrollState()),
        bottom = { Spacer(modifier = Modifier.height(contentPadding.calculateBottomPadding())) },
      )
    } else {
      HomeScreenCountdownsLazyVerticalGrid(modifier = modifier, contentPadding = contentPadding) {
        items(state.countdowns) { countdown -> HomeScreenCountdownItem(countdown) }
      }
    }
  }
}
