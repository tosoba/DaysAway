@file:OptIn(ExperimentalTime::class)

package com.trm.daysaway.widget.countdown

import com.kizitonwose.calendar.core.now
import com.trm.daysaway.core.base.util.LocalDateListSerializer
import com.trm.daysaway.core.domain.Countdown
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.serializers.LocalDateIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
sealed interface CountdownWidgetState {
  @Serializable data object Empty : CountdownWidgetState

  @Serializable data object Loading : CountdownWidgetState

  @Serializable
  data class Ready(
    @Serializable(with = LocalDateIso8601Serializer::class) val targetDate: LocalDate,
    val targetName: String?,
    @Serializable(with = LocalDateListSerializer::class) val excludedDates: List<LocalDate>,
    val version: Int = 1,
  ) : CountdownWidgetState {
    val formattedTargetDate: String
      get() = targetDate.format(LocalDate.Formats.ISO)

    fun getDaysRemaining(fromDate: LocalDate = LocalDate.now()): Long =
      targetDate.toEpochDays() - fromDate.toEpochDays() - excludedDates.count { it >= fromDate }

    constructor(
      countdown: Countdown
    ) : this(
      targetDate = countdown.targetDate,
      targetName = countdown.targetName,
      excludedDates = countdown.excludedDates,
    )
  }
}
