package com.trm.daysaway.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.trm.daysaway.DaysAwayApp
import java.time.Duration

class WidgetUpdateWorker(private val context: Context, workerParameters: WorkerParameters) :
  CoroutineWorker(context, workerParameters) {
  override suspend fun doWork(): Result {
    (context.applicationContext as DaysAwayApp).widgetManager.updateAllCountdownWidgets()
    return Result.success()
  }

  internal companion object {
    fun workRequest(): PeriodicWorkRequest =
      PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
          Duration.ofMillis(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS)
        )
        .build()

    const val WORK_NAME = "WidgetUpdateWork"
  }
}
