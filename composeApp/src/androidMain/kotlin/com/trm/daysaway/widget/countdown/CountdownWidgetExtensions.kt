package com.trm.daysaway.widget.countdown

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.trm.daysaway.core.base.util.actionIntent
import com.trm.daysaway.core.domain.Countdown
import com.trm.daysaway.ui.widget.WIDGET_PIN_SUCCESS_ACTION
import kotlinx.datetime.LocalDate

internal fun Context.countdownWidgetPinnedCallback(countdown: Countdown): PendingIntent =
  PendingIntent.getBroadcast(
    this,
    0,
    Intent(WIDGET_PIN_SUCCESS_ACTION).putCountdownExtras(countdown),
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
  )

internal fun Context.updateCountdownWidgetIntent(widgetId: Int, countdown: Countdown): Intent =
  actionIntent<CountdownWidgetReceiver>(CountdownWidgetActions.UPDATE)
    .putExtra(CountdownWidgetExtras.WIDGET_ID, widgetId)
    .putCountdownExtras(countdown)

internal fun Intent.putCountdownExtras(countdown: Countdown): Intent =
  putExtra(CountdownWidgetExtras.TARGET_NAME, countdown.targetName)
    .putExtra(CountdownWidgetExtras.TARGET_DATE, countdown.targetDate.toEpochDays())
    .putExtra(
      CountdownWidgetExtras.EXCLUDED_DATES,
      countdown.excludedDates.map(LocalDate::toEpochDays).toLongArray(),
    )

internal fun Bundle.toCountdown(): Countdown =
  Countdown(
    targetDate = LocalDate.fromEpochDays(getLong(CountdownWidgetExtras.TARGET_DATE)),
    targetName = getString(CountdownWidgetExtras.TARGET_NAME),
    excludedDates =
      getLongArray(CountdownWidgetExtras.EXCLUDED_DATES)?.map(LocalDate::fromEpochDays).orEmpty(),
  )
