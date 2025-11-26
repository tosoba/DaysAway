@file:OptIn(ExperimentalTime::class)

package com.trm.daysaway.widget.countdown

import androidx.compose.runtime.Composable
import androidx.glance.GlanceTheme
import androidx.glance.currentState
import androidx.glance.text.Text
import com.kizitonwose.calendar.core.now
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate

@Composable
fun CountdownWidgetContent() {
  GlanceTheme {
    // TODO: edit button for both cases
    when (val state = currentState<CountdownWidgetState>()) {
      CountdownWidgetState.Empty -> {
        Text("No target chosen")
      }
      is CountdownWidgetState.Ready -> {
        val daysRemaining = state.getDaysRemaining(LocalDate.now())
        Text(
          when {
            daysRemaining > 0 -> {
              "$daysRemaining ${if (daysRemaining == 1L) "day" else "days"} remaining until ${state.targetName ?: state.formattedTargetDate}"
            }
            !state.targetName.isNullOrBlank() -> {
              "$${state.targetName} was reached at ${state.formattedTargetDate}."
            }
            else -> {
              "${state.formattedTargetDate} was reached."
            }
          }
        )
      }
    }
  }
}
