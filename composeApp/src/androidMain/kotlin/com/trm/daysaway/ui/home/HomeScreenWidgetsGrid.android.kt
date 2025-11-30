package com.trm.daysaway.ui.home

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.FileObserver
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.datastore.dataStoreFile
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.trm.daysaway.core.base.util.getLastWidgetId
import com.trm.daysaway.widget.countdown.CountdownWidgetReceiver

private const val WIDGET_HOST_ID = 1024

@Composable
actual fun HomeScreenWidgetsGrid(modifier: Modifier) {
  val context = LocalContext.current
  val widgetIds = rememberSaveable { mutableStateListOf<Int>() }

  DisposableEffect(Unit) {
    val observer =
      object : FileObserver(context.dataStoreFile(""), DELETE or DELETE_SELF) {
          override fun onEvent(event: Int, path: String?) {
            when (event) {
              DELETE_SELF -> widgetIds.clear()
              DELETE -> widgetIdFrom(path)?.let(widgetIds::remove)
            }
          }

          private fun widgetIdFrom(path: String?): Int? =
            path
              ?.lastIndexOf('-')
              ?.takeUnless { it == -1 }
              ?.let { path.substring(it + 1) }
              ?.toIntOrNull()
        }
        .apply(FileObserver::startWatching)

    onDispose(observer::stopWatching)
  }

  LifecycleResumeEffect(Unit) {
    context
      .getLastWidgetId<CountdownWidgetReceiver>()
      ?.takeIf { widgetIds.lastOrNull() != it }
      ?.let(widgetIds::add)
    onPauseOrDispose {}
  }

  val appWidgetHost = remember { AppWidgetHost(context, WIDGET_HOST_ID) }
  val widgetProvider = remember {
    val componentName =
      ComponentName(
        context.applicationContext.packageName,
        CountdownWidgetReceiver::class.java.name,
      )
    AppWidgetManager.getInstance(context)
      .getInstalledProvidersForPackage(context.packageName, null)
      .find { it.provider == componentName }
  }

  LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 150.dp),
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(horizontal = 16.dp),
  ) {
    items(widgetIds, key = { it }) { widgetId ->
      AndroidView(
        factory = { ctx ->
          appWidgetHost.createView(ctx, widgetId, widgetProvider).apply {
            setAppWidget(widgetId, widgetProvider)
          }
        },
        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
      )
    }
  }
}
