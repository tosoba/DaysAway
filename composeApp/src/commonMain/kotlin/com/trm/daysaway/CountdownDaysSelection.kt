@file:OptIn(ExperimentalTime::class)

package com.trm.daysaway

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusDays
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

@Stable
class CountdownDaysSelection(
  endDate: LocalDate = LocalDate.now(),
  excludedDays: List<LocalDate> = emptyList(),
) {
  var endDate by mutableStateOf(endDate)
  private val excludedDates = mutableStateSetOf<LocalDate>().apply { addAll(excludedDays) }

  @Composable
  fun isDateSelected(date: LocalDate): Boolean = date <= endDate && date !in excludedDates

  val daysBetween: Long
    @Composable get() = endDate.toEpochDays() - LocalDate.now().toEpochDays() - excludedDates.size

  val isValid: Boolean
    @Composable get() = endDate != LocalDate.now()

  fun clear() {
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
    clearSelectionIfInvalid(today)
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
    clearSelectionIfInvalid(today)
  }

  private fun clearSelectionIfInvalid(today: LocalDate) {
    if (endDate.toEpochDays() - today.toEpochDays() - excludedDates.size == 0L) {
      clear()
    }
  }

  companion object {
    val Saver: Saver<CountdownDaysSelection, *> =
      listSaver(
        save = {
          buildList {
            add(it.endDate)
            addAll(it.excludedDates)
          }
        },
        restore = { CountdownDaysSelection(endDate = it.first(), excludedDays = it.drop(1)) },
      )
  }
}
