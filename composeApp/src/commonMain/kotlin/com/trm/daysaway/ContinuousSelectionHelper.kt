package com.trm.daysaway

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil

data class DateSelection(val startDate: LocalDate? = null, val endDate: LocalDate? = null) {
  companion object {
    private const val START_DATE_KEY = "startDate"
    private const val END_DATE_KEY = "endDate"

    val Saver: Saver<MutableState<DateSelection>, *> =
      mapSaver(
        save = {
          mapOf(
            START_DATE_KEY to it.value.startDate?.toEpochDays(),
            END_DATE_KEY to it.value.endDate?.toEpochDays(),
          )
        },
        restore = {
          mutableStateOf(
            DateSelection(
              startDate = (it[START_DATE_KEY] as? Long)?.let(LocalDate::fromEpochDays),
              endDate = (it[END_DATE_KEY] as? Long)?.let(LocalDate::fromEpochDays),
            )
          )
        },
      )
  }

  val daysBetween: Int?
    @Composable
    get() = if (startDate == null || endDate == null) null else startDate.daysUntil(endDate)
}

object ContinuousSelectionHelper {
  fun getSelection(clickedDate: LocalDate, dateSelection: DateSelection): DateSelection {
    val (selectionStartDate, selectionEndDate) = dateSelection
    return if (selectionStartDate != null) {
      if (clickedDate < selectionStartDate || selectionEndDate != null) {
        DateSelection(startDate = clickedDate, endDate = null)
      } else if (clickedDate != selectionStartDate) {
        DateSelection(startDate = selectionStartDate, endDate = clickedDate)
      } else {
        DateSelection(startDate = clickedDate, endDate = null)
      }
    } else {
      DateSelection(startDate = clickedDate, endDate = null)
    }
  }
}
