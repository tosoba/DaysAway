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
  targetDate: LocalDate = LocalDate.now(),
  excludedDays: List<LocalDate> = emptyList(),
) {
  var targetName by mutableStateOf(targetName)
  var targetDate by mutableStateOf(targetDate)
  private val excludedDates = mutableStateSetOf<LocalDate>().apply { addAll(excludedDays) }

  @Composable
  fun isDateIncluded(date: LocalDate): Boolean = date <= targetDate && date !in excludedDates

  private val daysRemaining: Long
    @Composable
    get() = targetDate.toEpochDays() - LocalDate.now().toEpochDays() - excludedDates.size

  val targetDateValid: Boolean
    @Composable get() = targetDate != LocalDate.now()

  val targetDescription: String
    @Composable
    get() =
      if (daysRemaining > 0) {
        "$daysRemaining ${if (daysRemaining == 1L) "day" else "days"} remaining until ${targetName ?: targetDate.format(LocalDate.Formats.ISO)}"
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
    targetDate = LocalDate.now()
    excludedDates.clear()
  }

  @Composable
  fun includes(dayOfWeek: DayOfWeek): Boolean {
    var date = LocalDate.now()
    while (date <= targetDate) {
      if (date.dayOfWeek == dayOfWeek && date !in excludedDates) return true
      date = date.plusDays(1)
    }
    return false
  }

  fun onDateIncludedChange(date: LocalDate) {
    val today = LocalDate.now()
    when {
      date > targetDate -> {
        targetDate = date
      }
      date == targetDate -> {
        var newTargetDate = targetDate
        do {
          newTargetDate = newTargetDate.minusDays(1).coerceAtLeast(today)
        } while (newTargetDate > today && newTargetDate in excludedDates)
        targetDate = newTargetDate
        excludedDates.removeAll { it > targetDate }
      }
      else -> {
        if (!excludedDates.remove(date)) {
          excludedDates.add(date)
        }
      }
    }
    resetIfInvalid(today)
  }

  fun onDayOfWeekIncludedChange(dayOfWeek: DayOfWeek, included: Boolean) {
    val today = LocalDate.now()
    var date = today
    while (date.dayOfWeek != dayOfWeek) {
      date = date.plusDays(1)
    }
    while (date < targetDate) {
      if (included) excludedDates.remove(date) else excludedDates.add(date)
      date = date.plusDays(7)
    }
    resetIfInvalid(today)
  }

  private fun resetIfInvalid(today: LocalDate) {
    if (targetDate.toEpochDays() - today.toEpochDays() - excludedDates.size == 0L) {
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
                add(it.targetDate)
                addAll(it.excludedDates)
              },
            )
          }
        },
        restore = {
          val dates = it[DATES] as List<*>
          WidgetScreenState(
            targetName = it[TARGET_NAME] as? String,
            targetDate = dates.first() as LocalDate,
            excludedDays = dates.drop(1).filterIsInstance<LocalDate>(),
          )
        },
      )
  }
}
