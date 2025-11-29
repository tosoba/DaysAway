@file:OptIn(ExperimentalTime::class)

package com.trm.daysaway.widget.countdown

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextDefaults
import com.kizitonwose.calendar.core.now
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate

@Composable
fun CountdownWidgetContent() {
  GlanceTheme {
    // TODO: edit button for both cases
    Box(
      modifier =
        GlanceModifier.fillMaxSize()
          .padding(16.dp)
          .appWidgetBackground()
          .background(GlanceTheme.colors.primaryContainer)
          .appWidgetBackgroundCornerRadius(),
      contentAlignment = Alignment.Center,
    ) {
      when (val state = currentState<CountdownWidgetState>()) {
        CountdownWidgetState.Empty -> {
          Text(
            text = "No target chosen",
            style = TextDefaults.defaultTextStyle.copy(textAlign = TextAlign.Center),
          )
        }
        is CountdownWidgetState.Ready -> {
          val daysRemaining = state.getDaysRemaining(LocalDate.now())
          Text(
            text =
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
              },
            style = TextDefaults.defaultTextStyle.copy(textAlign = TextAlign.Center),
          )
        }
      }
    }
  }
}

private fun GlanceModifier.appWidgetBackgroundCornerRadius(): GlanceModifier {
  if (Build.VERSION.SDK_INT >= 31) {
    cornerRadius(android.R.dimen.system_app_widget_background_radius)
  } else {
    cornerRadius(16.dp)
  }
  return this
}
