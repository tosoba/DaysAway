package com.trm.daysaway.core.base.util

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth

fun YearMonth.displayText(short: Boolean = false): String =
  "${month.displayText(short = short)} $year"

fun Month.displayText(short: Boolean = true): String = getDisplayName(short, Locale.current)

fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String =
  getDisplayName(narrow, Locale.current).let { value ->
    if (uppercase) value.toUpperCase(Locale.current) else value
  }

expect fun Month.getDisplayName(short: Boolean, locale: Locale): String

expect fun DayOfWeek.getDisplayName(narrow: Boolean = false, locale: Locale): String
