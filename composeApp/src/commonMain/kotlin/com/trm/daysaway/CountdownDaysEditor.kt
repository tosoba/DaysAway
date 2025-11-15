@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalTime::class)

package com.trm.daysaway

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import com.trm.daysaway.core.base.util.displayText
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

@Composable
fun CountdownDaysEditor(modifier: Modifier = Modifier, close: () -> Unit = {}) {
  val currentMonth = remember { YearMonth.now() }
  val today = remember { LocalDate.now() }
  val selection =
    rememberSaveable(saver = CountdownDaysSelection.Saver, init = ::CountdownDaysSelection)
  val daysOfWeek = remember(::daysOfWeek)

  Box(modifier = modifier) {
    Column {
      CalendarTop(
        daysOfWeek = daysOfWeek,
        selection = selection,
        close = close,
        clearDates = selection.selectedDates::clear,
      )

      VerticalCalendar(
        state =
          rememberCalendarState(
            startMonth = currentMonth,
            endMonth = currentMonth.plusMonths(12),
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
          ),
        contentPadding = PaddingValues(bottom = 100.dp, start = 8.dp, end = 8.dp),
        dayContent = { day ->
          ToggleDayButton(
            day = day,
            today = today,
            selection = selection,
            modifier =
              Modifier.fillMaxSize()
                .padding(
                  vertical = 2.dp,
                  horizontal =
                    if (
                      day.date.dayOfWeek != daysOfWeek.first() &&
                        day.date.dayOfWeek != daysOfWeek.last()
                    ) {
                      2.dp
                    } else {
                      0.dp
                    },
                ),
            onClick = { selection.onDateSelectionChange(it.date) },
          )
        },
        monthHeader = { month -> MonthHeader(month) },
      )
    }

    CalendarBottom(
      modifier =
        Modifier.wrapContentHeight()
          .fillMaxWidth()
          .background(Color.White)
          .align(Alignment.BottomCenter),
      selection = selection,
      save = {},
    )
  }
}

@Composable
private fun ToggleDayButton(
  day: CalendarDay,
  today: LocalDate,
  selection: CountdownDaysSelection,
  modifier: Modifier = Modifier,
  onClick: (CalendarDay) -> Unit,
) {
  if (day.position != DayPosition.MonthDate) return

  val enabled = day.position == DayPosition.MonthDate && day.date >= today
  val inSelection = selection.selectedDates.contains(day.date)

  ToggleButton(
    enabled = enabled,
    checked = enabled && inSelection,
    modifier = modifier,
    onCheckedChange = { onClick(day) },
  ) {
    Box(modifier = Modifier.heightIn(max = 64.dp), contentAlignment = Alignment.Center) {
      ToggleButtonText(
        text = day.date.day.toString(),
        color = if (enabled && inSelection) Color.White else Color.DarkGray,
      )
    }
  }
}

@Composable
private fun MonthHeader(calendarMonth: CalendarMonth) {
  Box(
    modifier =
      Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
  ) {
    Text(
      textAlign = TextAlign.Center,
      text = calendarMonth.yearMonth.displayText(),
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold,
    )
  }
}

@Composable
private fun CalendarTop(
  modifier: Modifier = Modifier,
  daysOfWeek: List<DayOfWeek>,
  selection: CountdownDaysSelection,
  close: () -> Unit,
  clearDates: () -> Unit,
) {
  Column(modifier.fillMaxWidth()) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 10.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Row(
        modifier = Modifier.height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          modifier =
            Modifier.fillMaxHeight()
              .aspectRatio(1f)
              .clip(CircleShape)
              .clickable(onClick = close)
              .padding(12.dp),
          imageVector = Icons.Default.Close,
          contentDescription = "Close",
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
          modifier =
            Modifier.clip(RoundedCornerShape(percent = 50))
              .clickable(onClick = clearDates)
              .padding(horizontal = 16.dp, vertical = 8.dp),
          text = "Clear",
          fontWeight = FontWeight.Medium,
          textAlign = TextAlign.End,
        )
      }
      val daysBetween = selection.selectedDates.size.takeIf { it > 0 }
      val text =
        if (daysBetween == null) {
          "Select dates"
        } else {
          "$daysBetween ${if (daysBetween == 1) "day" else "days"}"
        }
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
      )
      Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp, start = 4.dp)) {
        for (dayOfWeek in daysOfWeek) {
          val checked = selection.containsDayOfWeek(dayOfWeek)
          ToggleButton(checked = checked, onCheckedChange = {}, modifier = Modifier.weight(1f)) {
            ToggleButtonText(
              text = dayOfWeek.displayText(uppercase = true, narrow = true),
              color = if (checked) Color.White else Color.DarkGray,
            )
          }

          Spacer(modifier = Modifier.width(4.dp))
        }
      }
    }
    HorizontalDivider()
  }
}

@Composable
private fun ToggleButtonText(text: String, color: Color, modifier: Modifier = Modifier) {
  Text(
    modifier = modifier,
    textAlign = TextAlign.Center,
    color = color,
    text = text,
    style = MaterialTheme.typography.labelLarge,
  )
}

@Composable
private fun CalendarBottom(
  modifier: Modifier = Modifier,
  selection: CountdownDaysSelection,
  save: () -> Unit,
) {
  Column(modifier.fillMaxWidth()) {
    HorizontalDivider()
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      Text(text = "€75 night", fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.weight(1f))
      Button(
        modifier = Modifier.height(40.dp).width(100.dp),
        onClick = save,
        enabled = selection.selectedDates.size > 1,
      ) {
        Text(text = "Save")
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun CountdownDaysEditorPreview() {
  CountdownDaysEditor()
}
