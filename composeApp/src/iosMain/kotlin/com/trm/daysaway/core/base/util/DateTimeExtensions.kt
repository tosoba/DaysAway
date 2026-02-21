package com.trm.daysaway.core.base.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import platform.Foundation.NSDate

@Suppress("unused") // Used from swift.
fun localDateToNSDate(date: LocalDate): NSDate {
  val unixSeconds = date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds().toDouble() / 1000.0
  val referenceOffset = 978307200.0 // seconds between 1970-01-01 and 2001-01-01
  return NSDate(timeIntervalSinceReferenceDate = unixSeconds - referenceOffset)
}
