@file:OptIn(ExperimentalTime::class)

package com.trm.daysaway.widget.countdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.CircularProgressIndicator
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
import com.trm.daysaway.R
import com.trm.daysaway.core.base.util.pluralStringResource
import com.trm.daysaway.core.base.util.stringResource
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate

@Composable
fun CountdownWidgetContent() {
  GlanceTheme {
    Column(
      modifier =
        GlanceModifier.fillMaxSize()
          .padding(16.dp)
          .appWidgetBackground()
          .background(GlanceTheme.colors.primaryContainer)
          .cornerRadius(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      when (val state = currentState<CountdownWidgetState>()) {
        CountdownWidgetState.Empty -> {
          Text(
            text = stringResource(R.string.countdown_widget_no_target_chosen),
            style = mediumTextStyle(),
          )
        }
        CountdownWidgetState.Loading -> {
          CircularProgressIndicator()
        }
        is CountdownWidgetState.Ready -> {
          val daysRemaining = state.getDaysRemaining(LocalDate.now())
          when {
            daysRemaining > 0 -> {
              Text(
                text =
                  pluralStringResource(
                    R.plurals.countdown_widget_days,
                    daysRemaining.toInt(),
                    listOf(daysRemaining),
                  ),
                style = boldTextStyle(),
              )
              Text(
                text = stringResource(R.string.countdown_widget_remaining_until),
                style = regularTextStyle(),
              )
              Text(
                text = "${state.targetName ?: state.formattedTargetDate}.",
                style = mediumTextStyle(),
              )
            }
            !state.targetName.isNullOrBlank() -> {
              Text(text = state.targetName, style = mediumTextStyle())
              Text(
                text = stringResource(R.string.countdown_widget_was_reached_on),
                style = regularTextStyle(),
              )
              Text(text = "${state.formattedTargetDate}.", style = mediumTextStyle())
            }
            else -> {
              Text(text = state.formattedTargetDate, style = mediumTextStyle())
              Text(
                text = stringResource(R.string.countdown_widget_was_reached),
                style = regularTextStyle(),
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun regularTextStyle(): TextStyle =
  TextDefaults.defaultTextStyle.copy(
    color = GlanceTheme.colors.onBackground,
    textAlign = TextAlign.Center,
  )

@Composable
private fun mediumTextStyle(): TextStyle =
  TextDefaults.defaultTextStyle.copy(
    color = GlanceTheme.colors.onBackground,
    textAlign = TextAlign.Center,
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
  )

@Composable
private fun boldTextStyle(): TextStyle =
  TextDefaults.defaultTextStyle.copy(
    color = GlanceTheme.colors.onBackground,
    textAlign = TextAlign.Center,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
  )
