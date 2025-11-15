package com.trm.daysaway

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.plusDays
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Stable
class CountdownDaysSelection(val dates: List<LocalDate> = emptyList()) {
  val selectedDates = mutableStateSetOf<LocalDate>().apply { addAll(dates) }

  @Composable
  fun containsDayOfWeek(dayOfWeek: DayOfWeek): Boolean =
    selectedDates.any { it.dayOfWeek == dayOfWeek }

  fun onDateSelectionChange(date: LocalDate) {
    when {
      selectedDates.isEmpty() -> selectedDates.add(date)
      selectedDates.size == 1 -> {
        if (date < selectedDates.first()) {
          selectedDates.clear()
          selectedDates.add(date)
        } else {
          var dateToAdd = selectedDates.first()
          do {
            dateToAdd = dateToAdd.plusDays(1)
            selectedDates.add(dateToAdd)
          } while (date > dateToAdd)
        }
      }
      date < selectedDates.minBy(LocalDate::toEpochDays).minusDays(1) ||
        date > selectedDates.maxBy(LocalDate::toEpochDays).plusDays(1) -> {
        selectedDates.clear()
        selectedDates.add(date)
      }
      else -> {
        if (!selectedDates.remove(date)) {
          selectedDates.add(date)
        }
      }
    }
  }

  companion object Companion {
    val Saver: Saver<CountdownDaysSelection, *> =
      listSaver(save = { it.selectedDates.toList() }, restore = ::CountdownDaysSelection)
  }
}
