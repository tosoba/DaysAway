@file:OptIn(ExperimentalTime::class)

package com.trm.daysaway.widget.countdown

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextDefaults
import androidx.glance.text.TextStyle
import com.kizitonwose.calendar.core.now
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate

@Composable
fun CountdownWidgetContent() {
  GlanceTheme {
    // TODO: edit button for both cases
    Column(
      modifier =
        GlanceModifier.fillMaxSize()
          .padding(16.dp)
          .appWidgetBackground()
          .background(GlanceTheme.colors.primaryContainer)
          .appWidgetBackgroundCornerRadius(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      when (val state = currentState<CountdownWidgetState>()) {
        CountdownWidgetState.Empty -> {
          Text(text = "No target chosen", style = mediumTextStyle())
        }
        is CountdownWidgetState.Ready -> {
          val daysRemaining = state.getDaysRemaining(LocalDate.now())
          when {
            daysRemaining > 0 -> {
              Text(
                text = "$daysRemaining ${if (daysRemaining == 1L) "day" else "days"}",
                style = boldTextStyle(),
              )
              Text(text = "remaining until", style = regularTextStyle())
              Text(
                text = "${state.targetName ?: state.formattedTargetDate}.",
                style = mediumTextStyle(),
              )
            }
            !state.targetName.isNullOrBlank() -> {
              Text(text = state.targetName, style = mediumTextStyle())
              Text(text = "was reached on", style = regularTextStyle())
              Text(text = "${state.formattedTargetDate}.", style = mediumTextStyle())
            }
            else -> {
              Text(text = state.formattedTargetDate, style = mediumTextStyle())
              Text(text = "was reached.", style = regularTextStyle())
            }
          }
        }
      }
    }
  }
}

@Composable
private fun regularTextStyle(): TextStyle =
  TextDefaults.defaultTextStyle.copy(textAlign = TextAlign.Center)

@Composable
private fun mediumTextStyle(): TextStyle =
  TextDefaults.defaultTextStyle.copy(
    textAlign = TextAlign.Center,
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
  )

@Composable
private fun boldTextStyle(): TextStyle =
  TextDefaults.defaultTextStyle.copy(
    textAlign = TextAlign.Center,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
  )

private fun GlanceModifier.appWidgetBackgroundCornerRadius(): GlanceModifier {
  if (Build.VERSION.SDK_INT >= 31) {
    cornerRadius(android.R.dimen.system_app_widget_background_radius)
  } else {
    cornerRadius(16.dp)
  }
  return this
}
