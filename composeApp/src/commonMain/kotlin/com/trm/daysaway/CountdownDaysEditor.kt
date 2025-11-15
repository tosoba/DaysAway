package com.trm.daysaway

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
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
import com.trm.daysaway.ContinuousSelectionHelper.isInDateBetweenSelection
import com.trm.daysaway.ContinuousSelectionHelper.isOutDateBetweenSelection
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

private val primaryColor = Color.Black.copy(alpha = 0.9f)
private val selectionColor = primaryColor
private val continuousSelectionColor = Color.LightGray.copy(alpha = 0.3f)

@OptIn(ExperimentalTime::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CountdownDaysEditor(
  modifier: Modifier = Modifier,
  close: () -> Unit = {},
  dateSelected: (startDate: LocalDate, endDate: LocalDate) -> Unit = { _, _ -> },
) {
  val currentMonth = remember { YearMonth.now() }
  val startMonth = remember { currentMonth }
  val endMonth = remember { currentMonth.plusMonths(12) }
  val today = remember { LocalDate.now() }
  var selection by rememberSaveable(saver = DateSelection.Saver) { mutableStateOf(DateSelection()) }
  val daysOfWeek = remember { daysOfWeek() }

  MaterialExpressiveTheme {
    Box(modifier = modifier) {
      Column {
        val state =
          rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
          )
        CalendarTop(
          daysOfWeek = daysOfWeek,
          selection = selection,
          close = close,
          clearDates = { selection = DateSelection() },
        )
        VerticalCalendar(
          state = state,
          contentPadding = PaddingValues(bottom = 100.dp, start = 8.dp, end = 8.dp),
          dayContent = { value ->
            Day(value, today = today, selection = selection) { day ->
              if (day.position == DayPosition.MonthDate && day.date >= today) {
                selection =
                  ContinuousSelectionHelper.getSelection(
                    clickedDate = day.date,
                    dateSelection = selection,
                  )
              }
            }
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
        save = {
          val (startDate, endDate) = selection
          if (startDate != null && endDate != null) {
            dateSelected(startDate, endDate)
          }
        },
      )
    }
  }
}

@Composable
private fun Day(
  day: CalendarDay,
  today: LocalDate,
  selection: DateSelection,
  onClick: (CalendarDay) -> Unit,
) {
  var textColor = Color.Transparent
  Box(
    contentAlignment = Alignment.Center,
    modifier =
      Modifier.fillMaxSize()
        .clickable(
          enabled = day.position == DayPosition.MonthDate && day.date >= today,
          showRipple = false,
          onClick = { onClick(day) },
        )
        .backgroundHighlight(
          day = day,
          today = today,
          selection = selection,
          selectionColor = selectionColor,
          continuousSelectionColor = continuousSelectionColor,
        ) {
          textColor = it
        },
  ) {
    Box(
      modifier = Modifier.heightIn(max = 64.dp).aspectRatio(1f),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = day.date.day.toString(),
        color = textColor,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun CalendarTop(
  modifier: Modifier = Modifier,
  daysOfWeek: List<DayOfWeek>,
  selection: DateSelection,
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
      val daysBetween = selection.daysBetween
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
          val enabled = selection.startDate != null && selection.endDate != null
          ToggleButton(
            checked = enabled,
            onCheckedChange = {},
            enabled = enabled,
            modifier = Modifier.weight(1f),
          ) {
            DayOfWeekText(
              dayOfWeek = dayOfWeek,
              color = if (enabled) Color.White else Color.DarkGray,
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
private fun DayOfWeekText(dayOfWeek: DayOfWeek, color: Color, modifier: Modifier = Modifier) {
  Text(
    modifier = modifier,
    textAlign = TextAlign.Center,
    color = color,
    text = dayOfWeek.displayText(narrow = true, uppercase = true),
    style = MaterialTheme.typography.labelLarge,
  )
}

@Composable
private fun CalendarBottom(
  modifier: Modifier = Modifier,
  selection: DateSelection,
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
        enabled = selection.daysBetween != null,
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

fun Modifier.backgroundHighlight(
  day: CalendarDay,
  today: LocalDate,
  selection: DateSelection,
  selectionColor: Color,
  continuousSelectionColor: Color,
  textColor: (Color) -> Unit,
): Modifier = composed {
  val (startDate, endDate) = selection
  val padding = 0.dp
  when (day.position) {
    DayPosition.MonthDate -> {
      when {
        day.date < today -> {
          textColor(Colors.example4GrayPast)
          this
        }
        startDate == day.date && endDate == null -> {
          textColor(Color.White)
          padding(padding).background(color = selectionColor, shape = CircleShape)
        }
        day.date == startDate -> {
          textColor(Color.White)
          padding(vertical = padding)
            .background(color = continuousSelectionColor, shape = HalfSizeShape(clipStart = true))
            .padding(horizontal = padding)
            .background(color = selectionColor, shape = CircleShape)
        }
        startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
          textColor(Colors.example4Gray)
          padding(vertical = padding).background(color = continuousSelectionColor)
        }
        day.date == endDate -> {
          textColor(Color.White)
          padding(vertical = padding)
            .background(color = continuousSelectionColor, shape = HalfSizeShape(clipStart = false))
            .padding(horizontal = padding)
            .background(color = selectionColor, shape = CircleShape)
        }
        day.date == today -> {
          textColor(Colors.example4Gray)
          padding(padding)
            .border(width = 1.dp, shape = CircleShape, color = Colors.example4GrayPast)
        }
        else -> {
          textColor(Colors.example4Gray)
          this
        }
      }
    }
    DayPosition.InDate -> {
      textColor(Color.Transparent)
      if (
        startDate != null &&
          endDate != null &&
          isInDateBetweenSelection(day.date, startDate, endDate)
      ) {
        padding(vertical = padding).background(color = continuousSelectionColor)
      } else {
        this
      }
    }
    DayPosition.OutDate -> {
      textColor(Color.Transparent)
      if (
        startDate != null &&
          endDate != null &&
          isOutDateBetweenSelection(day.date, startDate, endDate)
      ) {
        padding(vertical = padding).background(color = continuousSelectionColor)
      } else {
        this
      }
    }
  }
}

private class HalfSizeShape(private val clipStart: Boolean) : Shape {
  override fun createOutline(
    size: Size,
    layoutDirection: LayoutDirection,
    density: Density,
  ): Outline {
    val half = size.width / 2f
    val offset =
      if (layoutDirection == LayoutDirection.Ltr) {
        if (clipStart) Offset(half, 0f) else Offset.Zero
      } else {
        if (clipStart) Offset.Zero else Offset(half, 0f)
      }
    return Outline.Rectangle(Rect(offset, Size(half, size.height)))
  }
}

fun YearMonth.displayText(short: Boolean = false): String =
  "${month.displayText(short = short)} $year"

fun Month.displayText(short: Boolean = true): String = getDisplayName(short, Locale.current)

fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String =
  getDisplayName(narrow, Locale.current).let { value ->
    if (uppercase) value.toUpperCase(Locale.current) else value
  }

expect fun Month.getDisplayName(short: Boolean, locale: Locale): String

expect fun DayOfWeek.getDisplayName(narrow: Boolean = false, locale: Locale): String
