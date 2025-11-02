package com.trm.daysaway

import androidx.compose.ui.text.intl.Locale
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale as JavaLocale
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import kotlinx.datetime.toJavaDayOfWeek
import kotlinx.datetime.toJavaMonth

actual fun Month.getDisplayName(short: Boolean, locale: Locale): String =
  toJavaMonth()
    .getDisplayName(
      if (short) JavaTextStyle.SHORT else JavaTextStyle.FULL,
      JavaLocale.forLanguageTag(locale.toLanguageTag()),
    )

actual fun DayOfWeek.getDisplayName(narrow: Boolean, locale: Locale): String =
  toJavaDayOfWeek()
    .getDisplayName(
      if (narrow) JavaTextStyle.NARROW else JavaTextStyle.SHORT,
      JavaLocale.forLanguageTag(locale.toLanguageTag()),
    )
