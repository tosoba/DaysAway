@file:OptIn(
  ExperimentalMaterial3ExpressiveApi::class,
  ExperimentalTime::class,
  ExperimentalMaterial3Api::class,
)

package com.trm.daysaway.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TwoRowsTopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import com.trm.daysaway.domain.Countdown
import daysaway.composeapp.generated.resources.Res
import daysaway.composeapp.generated.resources.RobotoMono
import daysaway.composeapp.generated.resources.cancel
import daysaway.composeapp.generated.resources.confirm
import daysaway.composeapp.generated.resources.reset
import daysaway.composeapp.generated.resources.widget_screen_choose_target_date
import daysaway.composeapp.generated.resources.widget_screen_confirm_target_date
import daysaway.composeapp.generated.resources.widget_screen_edit_name
import daysaway.composeapp.generated.resources.widget_screen_enter_custom_name
import daysaway.composeapp.generated.resources.widget_screen_go_back
import daysaway.composeapp.generated.resources.widget_screen_target_name
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@Composable
fun WidgetScreen(onConfirmClick: (Countdown) -> Unit, navigateBack: () -> Unit) {
  val state = rememberSaveable(saver = WidgetScreenState.Saver, init = ::WidgetScreenState)

  val scope = rememberCoroutineScope()
  var targetNameSheetVisible by rememberSaveable { mutableStateOf(false) }
  val targetNameSheetState = rememberModalBottomSheetState()
  val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
  val buttonHeight = ButtonDefaults.MediumContainerHeight

  WidgetScreenPinSuccessEffect(navigateBack)

  Scaffold(
    modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
    topBar = {
      TwoRowsTopAppBar(
        title = {
          Text(
            if (state.targetDateValid) stringResource(Res.string.widget_screen_confirm_target_date)
            else stringResource(Res.string.widget_screen_choose_target_date)
          )
        },
        subtitle = { Text(text = state.targetDescription) },
        actions = {
          IconButton(onClick = { targetNameSheetVisible = true }) {
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = stringResource(Res.string.widget_screen_edit_name),
            )
          }
        },
        collapsedHeight = 56.dp,
        expandedHeight = 108.dp,
        navigationIcon = {
          IconButton(onClick = navigateBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = stringResource(Res.string.widget_screen_go_back),
            )
          }
        },
        scrollBehavior = topAppBarScrollBehavior,
      )
    },
    bottomBar = {
      BottomAppBar {
        Spacer(modifier = Modifier.width(16.dp))

        TextButton(
          enabled = state.targetDateValid,
          onClick = state::reset,
          modifier = Modifier.heightIn(buttonHeight),
          contentPadding = ButtonDefaults.contentPaddingFor(buttonHeight),
        ) {
          Text(stringResource(Res.string.reset), style = ButtonDefaults.textStyleFor(buttonHeight))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
          enabled = state.targetDateValid,
          onClick = { onConfirmClick(state.toCountdown()) },
          modifier = Modifier.heightIn(buttonHeight),
          contentPadding = ButtonDefaults.contentPaddingFor(buttonHeight),
        ) {
          Text(
            stringResource(Res.string.confirm),
            style = ButtonDefaults.textStyleFor(buttonHeight),
          )
        }

        Spacer(modifier = Modifier.width(16.dp))
      }
    },
  ) { contentPadding ->
    val currentMonth = remember { YearMonth.now() }
    val today = remember { LocalDate.now() }
    val daysOfWeek = remember(::daysOfWeek)

    Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
      DayOfWeekToggleButtons(
        daysOfWeek = daysOfWeek,
        state = state,
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
            state = state,
            modifier =
              Modifier.fillMaxSize()
                .padding(
                  top = 2.dp,
                  bottom = 2.dp,
                  start = if (dayOfWeek == daysOfWeek.first()) 0.dp else 2.dp,
                  end = if (dayOfWeek == daysOfWeek.last()) 0.dp else 2.dp,
                ),
            onClick = { state.onDateIncludedChange(it.date) },
          )
        },
        monthHeader = { month -> MonthHeader(month) },
      )
    }

    if (targetNameSheetVisible) {
      ModalBottomSheet(
        onDismissRequest = { targetNameSheetVisible = false },
        sheetState = targetNameSheetState,
      ) {
        fun hideSheet() {
          scope
            .launch { targetNameSheetState.hide() }
            .invokeOnCompletion {
              if (!targetNameSheetState.isVisible) {
                targetNameSheetVisible = false
              }
            }
        }

        @Composable
        fun SheetVerticalSpacer() {
          Spacer(modifier = Modifier.height(16.dp))
        }

        var initialTargetName by rememberSaveable { mutableStateOf(state.targetName) }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          val sheetChildModifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)

          Text(
            stringResource(Res.string.widget_screen_enter_custom_name),
            style = MaterialTheme.typography.titleLargeEmphasized,
            modifier = sheetChildModifier,
          )

          SheetVerticalSpacer()

          OutlinedTextField(
            modifier = sheetChildModifier,
            value = state.targetName.orEmpty(),
            onValueChange = { state.targetName = it },
            label = { Text(stringResource(Res.string.widget_screen_target_name)) },
          )

          SheetVerticalSpacer()

          Row(modifier = sheetChildModifier, horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(
              modifier = Modifier.heightIn(buttonHeight),
              contentPadding = ButtonDefaults.contentPaddingFor(buttonHeight),
              onClick = {
                state.targetName = initialTargetName
                hideSheet()
              },
            ) {
              Text(stringResource(Res.string.cancel))
            }

            Button(
              modifier = Modifier.heightIn(buttonHeight),
              contentPadding = ButtonDefaults.contentPaddingFor(buttonHeight),
              onClick = ::hideSheet,
            ) {
              Text(stringResource(Res.string.confirm))
            }
          }

          SheetVerticalSpacer()
        }
      }
    }
  }
}

@Composable
private fun DayToggleButton(
  day: CalendarDay,
  today: LocalDate,
  state: WidgetScreenState,
  modifier: Modifier = Modifier,
  onClick: (CalendarDay) -> Unit,
) {
  if (day.position != DayPosition.MonthDate) return

  val enabled = day.position == DayPosition.MonthDate && day.date >= today
  val included = state.includes(day.date)

  ToggleButton(
    enabled = enabled,
    checked = enabled && included,
    contentPadding = PaddingValues(4.dp),
    modifier = modifier,
    onCheckedChange = { onClick(day) },
  ) {
    Box(contentAlignment = Alignment.Center) {
      ToggleButtonText(
        text = day.date.day.toString(),
        color =
          if (enabled && included) MaterialTheme.colorScheme.surface
          else MaterialTheme.colorScheme.onSurfaceVariant,
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
  state: WidgetScreenState,
  modifier: Modifier = Modifier,
) {
  Row(modifier = modifier) {
    daysOfWeek.forEachIndexed { index, dayOfWeek ->
      val checked = state.includes(dayOfWeek)

      ToggleButton(
        checked = checked,
        onCheckedChange = { state.onDayOfWeekIncludedChange(dayOfWeek = dayOfWeek, included = it) },
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier.weight(1f),
      ) {
        ToggleButtonText(
          text = dayOfWeek.displayText(uppercase = true, narrow = true),
          color =
            if (checked) MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.fillMaxWidth(),
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
    autoSize = TextAutoSize.StepBased(maxFontSize = MaterialTheme.typography.labelLarge.fontSize),
    style = MaterialTheme.typography.labelLarge,
    fontFamily = FontFamily(Font(Res.font.RobotoMono)),
  )
}

@Preview(showBackground = true)
@Composable
private fun WidgetScreenPreview() {
  WidgetScreen(onConfirmClick = {}, navigateBack = {})
}
