package com.trm.daysaway.domain

import kotlinx.datetime.LocalDate

data class Countdown(
  val targetDate: LocalDate,
  val targetName: String?,
  val excludedDates: List<LocalDate>,
)
