@file:OptIn(ExperimentalTime::class)

package com.trm.daysaway.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.text.Text
import com.kizitonwose.calendar.core.now
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

class CountdownWidget : GlanceAppWidget() {
  override val sizeMode: SizeMode = SizeMode.Exact

  override suspend fun provideGlance(context: Context, id: GlanceId) {
    provideContent {
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
  }
}
