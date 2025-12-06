package com.trm.daysaway.worker

import android.content.Context
import androidx.startup.Initializer
import androidx.work.*

class WidgetUpdateWorkerInitializer : Initializer<WidgetUpdateWorkerInitializer.Companion> {
  override fun create(context: Context): Companion {
    WorkManager.getInstance(context).apply {
      enqueueUniquePeriodicWork(
        WidgetUpdateWorker.WORK_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        WidgetUpdateWorker.workRequest(),
      )
    }
    return Companion
  }

  override fun dependencies(): List<Class<out Initializer<*>>> =
    listOf(WorkManagerInitializer::class.java)

  companion object Companion
}
