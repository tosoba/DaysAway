package com.trm.daysaway.ui.home

import android.os.Build
import android.os.FileObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStoreFile
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.trm.daysaway.core.base.util.getAllWidgetIds
import com.trm.daysaway.core.base.util.getLastWidgetId
import com.trm.daysaway.widget.countdown.CountdownWidgetReceiver

@Stable
actual class HomeScreenState(initialWidgetIds: List<Int> = emptyList()) {
  val widgetIds = initialWidgetIds.toMutableStateList()

  companion object {
    val Saver = listSaver(save = { it.widgetIds.toList() }, restore = ::HomeScreenState)
  }
}

@Composable
actual fun rememberHomeScreenState(vararg inputs: Any?): HomeScreenState {
  val context = LocalContext.current
  val state =
    rememberSaveable(
      inputs,
      saver = HomeScreenState.Saver,
      init = { HomeScreenState(context.getAllWidgetIds<CountdownWidgetReceiver>().toList()) },
    )

  DisposableEffect(Unit) {
    fun onFileEvent(event: Int, path: String?) {
      when (event) {
        FileObserver.DELETE_SELF -> {
          state.widgetIds.clear()
        }
        FileObserver.DELETE -> {
          path
            ?.lastIndexOf('-')
            ?.takeUnless { it == -1 }
            ?.let { path.substring(it + 1) }
            ?.toIntOrNull()
            ?.let(state.widgetIds::remove)
        }
      }
    }

    val eventsMask = FileObserver.DELETE or FileObserver.DELETE_SELF
    val dataStoreDir = context.dataStoreFile("")
    val observer =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          object : FileObserver(dataStoreDir, eventsMask) {
            override fun onEvent(event: Int, path: String?) {
              onFileEvent(event, path)
            }
          }
        } else {
          @Suppress("DEPRECATION")
          object : FileObserver(dataStoreDir.path, eventsMask) {
            override fun onEvent(event: Int, path: String?) {
              onFileEvent(event, path)
            }
          }
        }
        .apply(FileObserver::startWatching)

    onDispose(observer::stopWatching)
  }

  LifecycleResumeEffect(Unit) {
    context
      .getLastWidgetId<CountdownWidgetReceiver>()
      ?.takeIf { state.widgetIds.lastOrNull() != it }
      ?.let(state.widgetIds::add)
    onPauseOrDispose {}
  }

  return state
}
