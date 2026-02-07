package com.trm.daysaway.worker

import android.content.Context
import androidx.startup.Initializer
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer

class WidgetRefreshWorkerInitializer : Initializer<WidgetRefreshWorkerInitializer.Companion> {
  override fun create(context: Context): Companion {
    WorkManager.getInstance(context).apply {
      enqueueUniquePeriodicWork(
        WidgetRefreshWorker.WORK_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        WidgetRefreshWorker.workRequest(),
      )
    }
    return Companion
  }

  override fun dependencies(): List<Class<out Initializer<*>>> =
    listOf(WorkManagerInitializer::class.java)

  companion object Companion
}
