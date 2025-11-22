@file:OptIn(ExperimentalTime::class)

package com.trm.daysaway.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.setValue
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusDays
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlin.time.ExperimentalTime

@Stable
class WidgetScreenState(
  targetName: String? = null,
  endDate: LocalDate = LocalDate.now(),
  excludedDays: List<LocalDate> = emptyList(),
) {
  var targetName by mutableStateOf(targetName)
  var endDate by mutableStateOf(endDate)
  private val excludedDates = mutableStateSetOf<LocalDate>().apply { addAll(excludedDays) }

  @Composable
  fun isDateSelected(date: LocalDate): Boolean = date <= endDate && date !in excludedDates

  private val daysRemaining: Long
    @Composable get() = endDate.toEpochDays() - LocalDate.now().toEpochDays() - excludedDates.size

  val selectionValid: Boolean
    @Composable get() = endDate != LocalDate.now()

  val targetDescription: String
    @Composable
    get() =
      if (daysRemaining > 0) {
        "$daysRemaining ${if (daysRemaining == 1L) "day" else "days"} remaining until ${targetName ?: endDate.format(LocalDate.Formats.ISO)}"
      } else {
        buildString {
          append("No target date chosen")
          if (!targetName.isNullOrBlank()) {
            append(" for $targetName")
          }
        }
      }

  fun reset() {
    targetName = null
    endDate = LocalDate.now()
    excludedDates.clear()
  }

  @Composable
  fun containsDayOfWeek(dayOfWeek: DayOfWeek): Boolean {
    var date = LocalDate.now()
    while (date <= endDate) {
      if (date.dayOfWeek == dayOfWeek && date !in excludedDates) return true
      date = date.plusDays(1)
    }
    return false
  }

  fun onDateSelectionChange(date: LocalDate) {
    val today = LocalDate.now()
    when {
      date > endDate -> {
        endDate = date
      }
      date == endDate -> {
        var newEndDate = endDate
        do {
          newEndDate = newEndDate.minusDays(1).coerceAtLeast(today)
        } while (newEndDate > today && newEndDate in excludedDates)
        endDate = newEndDate
        excludedDates.removeAll { it > endDate }
      }
      else -> {
        if (!excludedDates.remove(date)) {
          excludedDates.add(date)
        }
      }
    }
    resetSelectionIfInvalid(today)
  }

  fun onDayOfWeekSelectionChange(dayOfWeek: DayOfWeek, selected: Boolean) {
    val today = LocalDate.now()
    var date = today
    while (date.dayOfWeek != dayOfWeek) {
      date = date.plusDays(1)
    }
    while (date < endDate) {
      if (selected) excludedDates.remove(date) else excludedDates.add(date)
      date = date.plusDays(7)
    }
    resetSelectionIfInvalid(today)
  }

  private fun resetSelectionIfInvalid(today: LocalDate) {
    if (endDate.toEpochDays() - today.toEpochDays() - excludedDates.size == 0L) {
      reset()
    }
  }

  companion object {
    private const val DATES = "DATES"
    private const val TARGET_NAME = "TARGET_NAME"

    val Saver: Saver<WidgetScreenState, *> =
      mapSaver(
        save = {
          buildMap {
            put(TARGET_NAME, it.targetName)
            put(
              DATES,
              buildList {
                add(it.endDate)
                addAll(it.excludedDates)
              },
            )
          }
        },
        restore = {
          val dates = it[DATES] as List<*>
          WidgetScreenState(
            targetName = it[TARGET_NAME] as? String,
            endDate = dates.first() as LocalDate,
            excludedDays = dates.drop(1).filterIsInstance<LocalDate>(),
          )
        },
      )
  }
}
