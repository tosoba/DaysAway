package com.trm.daysaway.core.base.util

import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import platform.Foundation.NSDate

@Suppress("unused") // Used from swift.
fun localDateToNSDate(date: LocalDate): NSDate {
  val unixSeconds = date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds().toDouble() / 1000.0
  return NSDate(timeIntervalSinceReferenceDate = unixSeconds - REFERENCE_OFFSET_SECONDS)
}

@Suppress("unused") // Used from swift.
fun nsDateToLocalDate(date: NSDate): LocalDate {
  val unixSeconds = date.timeIntervalSinceReferenceDate + REFERENCE_OFFSET_SECONDS
  val epochMillis = (unixSeconds * 1000.0).toLong()
  return Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(TimeZone.UTC).date
}

private const val REFERENCE_OFFSET_SECONDS = 978307200.0
