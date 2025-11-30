package com.trm.daysaway.widget.countdown

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

private const val WIDGET_HOST_ID = 1024

@Composable
fun CountdownWidgetPreviews(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val appWidgetManager = AppWidgetManager.getInstance(context)
  val appWidgetHost = remember { AppWidgetHost(context, WIDGET_HOST_ID) }

  DisposableEffect(appWidgetHost) {
    appWidgetHost.startListening()
    onDispose(appWidgetHost::stopListening)
  }

  val widgetProviders = remember {
    appWidgetManager.getInstalledProvidersForPackage(context.packageName, null)
  }
  val widgetProvider =
    remember(widgetProviders) {
      widgetProviders.find { it.provider.className == CountdownWidgetReceiver::class.java.name }
    }
  val widgetIds =
    remember(widgetProvider) {
      widgetProvider?.let { appWidgetManager.getAppWidgetIds(it.provider) }?.toList().orEmpty()
    }

  LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 150.dp),
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(horizontal = 16.dp),
  ) {
    items(widgetIds) { appWidgetId ->
      AndroidView(
        factory = { ctx ->
          appWidgetHost.createView(ctx, appWidgetId, widgetProvider).apply {
            setAppWidget(appWidgetId, widgetProvider)
          }
        },
        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
      )
    }
  }
}
