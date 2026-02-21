package com.trm.daysaway.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trm.daysaway.domain.Countdown
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format

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

@Composable
private fun HomeScreenCountdownItem(countdown: Countdown) {
  Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
    Column(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Text(countdown.targetDate.format(LocalDate.Formats.ISO))
    }
  }
}
