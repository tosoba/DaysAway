@file:OptIn(ExperimentalTime::class)

package com.trm.daysaway.widget.countdown

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.actionSendBroadcast
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextDefaults
import androidx.glance.text.TextStyle
import com.kizitonwose.calendar.core.now
import com.trm.daysaway.R
import daysaway.composeapp.generated.resources.Res
import daysaway.composeapp.generated.resources.countdown_widget_days
import daysaway.composeapp.generated.resources.countdown_widget_no_target_chosen
import daysaway.composeapp.generated.resources.countdown_widget_refresh
import daysaway.composeapp.generated.resources.countdown_widget_remaining_until
import daysaway.composeapp.generated.resources.countdown_widget_was_reached
import daysaway.composeapp.generated.resources.countdown_widget_was_reached_on
import daysaway.composeapp.generated.resources.refresh
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@Composable
fun CountdownWidgetContent(id: GlanceId) {
  CompositionLocalProvider(
    LocalConfiguration provides Configuration(),
    LocalDensity provides Density(1f),
  ) {
    GlanceTheme {
      Column(
        modifier =
          GlanceModifier.fillMaxSize()
            .padding(8.dp)
            .appWidgetBackground()
            .background(GlanceTheme.colors.primaryContainer)
            .cornerRadius(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        when (val state = currentState<CountdownWidgetState>()) {
          CountdownWidgetState.Empty -> {
            Text(
              text = stringResource(Res.string.countdown_widget_no_target_chosen),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text =
                      pluralStringResource(
                        resource = Res.plurals.countdown_widget_days,
                        quantity = daysRemaining.toInt(),
                        daysRemaining.toInt(),
                      ),
                    style = boldTextStyle(),
                  )

                  RefreshButton(id)
                }
                Text(
                  text = stringResource(Res.string.countdown_widget_remaining_until),
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
                  text = stringResource(Res.string.countdown_widget_was_reached_on),
                  style = regularTextStyle(),
                )
                Text(text = "${state.formattedTargetDate}.", style = mediumTextStyle())
              }
              else -> {
                Text(text = state.formattedTargetDate, style = mediumTextStyle())
                Text(
                  text = stringResource(Res.string.countdown_widget_was_reached),
                  style = regularTextStyle(),
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun RefreshButton(id: GlanceId) {
  val context = LocalContext.current
  val widgetManager = remember { GlanceAppWidgetManager(context) }
  Image(
    provider = ImageProvider(R.drawable.refresh),
    contentDescription = stringResource(Res.string.countdown_widget_refresh),
    colorFilter = ColorFilter.tint(GlanceTheme.colors.onBackground),
    modifier =
      GlanceModifier.padding(4.dp)
        .cornerRadius(4.dp)
        .clickable(
          actionSendBroadcast(
            context.refreshCountdownWidgetIntent(widgetManager.getAppWidgetId(id))
          )
        ),
  )
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
