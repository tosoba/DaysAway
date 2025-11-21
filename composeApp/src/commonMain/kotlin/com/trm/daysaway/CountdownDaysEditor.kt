@file:OptIn(
  ExperimentalMaterial3ExpressiveApi::class,
  ExperimentalTime::class,
  ExperimentalMaterial3Api::class,
)

package com.trm.daysaway

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TwoRowsTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.kizitonwose.calendar.core.plusYears
import com.trm.daysaway.core.base.util.displayText
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.format
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

@Composable
fun CountdownDaysEditor(onBackClick: () -> Unit = {}) {
  val currentMonth = remember { YearMonth.now() }
  val today = remember { LocalDate.now() }
  val selection =
    rememberSaveable(saver = CountdownDaysSelection.Saver, init = ::CountdownDaysSelection)
  val daysOfWeek = remember(::daysOfWeek)

  val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

  Scaffold(
    modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
    topBar = {
      TwoRowsTopAppBar(
        title = { Text(if (selection.isValid) "Confirm selection" else "Choose a target date") },
        subtitle = {
          val daysRemaining = selection.daysBetween.takeIf { it > 0 }
          Text(
            text =
              if (daysRemaining == null) {
                "No target date chosen"
              } else {
                "$daysRemaining ${if (daysRemaining == 1L) "day" else "days"} remaining until ${selection.endDate.format(LocalDate.Formats.ISO)}"
              }
          )
        },
        actions = {
          IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit name")
          }
        },
        collapsedHeight = 56.dp,
        expandedHeight = 108.dp,
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
          }
        },
        scrollBehavior = topAppBarScrollBehavior,
      )
    },
    bottomBar = {
      BottomAppBar {
        Spacer(modifier = Modifier.width(16.dp))

        val buttonHeight = ButtonDefaults.MediumContainerHeight

        TextButton(
          enabled = selection.isValid,
          onClick = selection::reset,
          modifier = Modifier.heightIn(buttonHeight),
          contentPadding = ButtonDefaults.contentPaddingFor(buttonHeight),
        ) {
          Text("Reset", style = ButtonDefaults.textStyleFor(buttonHeight))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
          enabled = selection.isValid,
          onClick = {},
          modifier = Modifier.heightIn(buttonHeight),
          contentPadding = ButtonDefaults.contentPaddingFor(buttonHeight),
        ) {
          Text("Next", style = ButtonDefaults.textStyleFor(buttonHeight))
        }

        Spacer(modifier = Modifier.width(16.dp))
      }
    },
  ) { contentPadding ->
    Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
      DayOfWeekToggleButtons(
        daysOfWeek = daysOfWeek,
        selection = selection,
        modifier = Modifier.fillMaxWidth().padding(8.dp),
      )

      HorizontalDivider(modifier = Modifier.fillMaxWidth())

      VerticalCalendar(
        modifier = Modifier.fillMaxWidth().weight(1f),
        state =
          rememberCalendarState(
            startMonth = currentMonth,
            endMonth = currentMonth.plusYears(100),
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
          ),
        contentPadding = PaddingValues(horizontal = 8.dp),
        dayContent = { day ->
          val dayOfWeek = day.date.dayOfWeek
          DayToggleButton(
            day = day,
            today = today,
            selection = selection,
            modifier =
              Modifier.fillMaxSize()
                .padding(
                  vertical = 2.dp,
                  horizontal =
                    if (dayOfWeek != daysOfWeek.first() && dayOfWeek != daysOfWeek.last()) {
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
  }
}

@Composable
private fun DayToggleButton(
  day: CalendarDay,
  today: LocalDate,
  selection: CountdownDaysSelection,
  modifier: Modifier = Modifier,
  onClick: (CalendarDay) -> Unit,
) {
  if (day.position != DayPosition.MonthDate) return

  val enabled = day.position == DayPosition.MonthDate && day.date >= today
  val selected = selection.isDateSelected(day.date)

  ToggleButton(
    enabled = enabled,
    checked = enabled && selected,
    modifier = modifier,
    onCheckedChange = { onClick(day) },
  ) {
    Box(modifier = Modifier.heightIn(max = 64.dp), contentAlignment = Alignment.Center) {
      ToggleButtonText(
        text = day.date.day.toString(),
        color = if (enabled && selected) Color.White else Color.DarkGray,
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
private fun DayOfWeekToggleButtons(
  daysOfWeek: List<DayOfWeek>,
  selection: CountdownDaysSelection,
  modifier: Modifier = Modifier,
) {
  Row(modifier = modifier) {
    daysOfWeek.forEachIndexed { index, dayOfWeek ->
      val checked = selection.containsDayOfWeek(dayOfWeek)

      ToggleButton(
        checked = checked,
        onCheckedChange = {
          selection.onDayOfWeekSelectionChange(dayOfWeek = dayOfWeek, selected = it)
        },
        modifier = Modifier.weight(1f),
      ) {
        ToggleButtonText(
          text = dayOfWeek.displayText(uppercase = true, narrow = true),
          color = if (checked) Color.White else Color.DarkGray,
        )
      }

      if (index != daysOfWeek.lastIndex) {
        Spacer(modifier = Modifier.width(4.dp))
      }
    }
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

@Preview(showBackground = true)
@Composable
private fun CountdownDaysEditorPreview() {
  CountdownDaysEditor()
}
