package com.trm.daysaway.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.now
import com.trm.daysaway.core.base.util.getDaysRemainingUntil
import com.trm.daysaway.domain.Countdown
import daysaway.composeapp.generated.resources.Res
import daysaway.composeapp.generated.resources.countdown_widget_days
import daysaway.composeapp.generated.resources.countdown_widget_remaining_until
import daysaway.composeapp.generated.resources.countdown_widget_was_reached
import daysaway.composeapp.generated.resources.countdown_widget_was_reached_on
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreenCountdownItem(countdown: Countdown) {
  Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
    Column(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      val daysRemaining =
        LocalDate.now().getDaysRemainingUntil(countdown.targetDate, countdown.excludedDates)
      when {
        daysRemaining > 0 -> {
          Text(
            text =
              pluralStringResource(
                resource = Res.plurals.countdown_widget_days,
                quantity = daysRemaining.toInt(),
                daysRemaining.toInt(),
              ),
            style = MaterialTheme.typography.titleLarge,
          )
          Text(
            text = stringResource(Res.string.countdown_widget_remaining_until),
            style = MaterialTheme.typography.bodySmall,
          )
          Text(
            text = "${countdown.targetName ?: countdown.targetDate.format(LocalDate.Formats.ISO)}.",
            style = MaterialTheme.typography.bodyMedium,
          )
        }
        !countdown.targetName.isNullOrBlank() -> {
          Text(text = countdown.targetName, style = MaterialTheme.typography.bodyMedium)
          Text(
            text = stringResource(Res.string.countdown_widget_was_reached_on),
            style = MaterialTheme.typography.bodySmall,
          )
          Text(
            text = "${countdown.targetDate.format(LocalDate.Formats.ISO)}.",
            style = MaterialTheme.typography.bodyMedium,
          )
        }
        else -> {
          Text(
            text = countdown.targetDate.format(LocalDate.Formats.ISO),
            style = MaterialTheme.typography.bodyMedium,
          )
          Text(
            text = stringResource(Res.string.countdown_widget_was_reached),
            style = MaterialTheme.typography.bodySmall,
          )
        }
      }
    }
  }
}
