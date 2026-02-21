package com.trm.daysaway.ui.home

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.trm.daysaway.widget.countdown.CountdownWidgetReceiver

private const val WIDGET_HOST_ID = 1024

@Composable
actual fun HomeScreenCountdowns(
  state: HomeScreenState,
  contentPadding: PaddingValues,
  modifier: Modifier,
) {
  val context = LocalContext.current
  val appWidgetHost = remember(state.refreshCount) { AppWidgetHost(context, WIDGET_HOST_ID) }
  val widgetProvider =
    remember(state.refreshCount) {
      val componentName =
        ComponentName(
          context.applicationContext.packageName,
          CountdownWidgetReceiver::class.java.name,
        )
      AppWidgetManager.getInstance(context)
        .getInstalledProvidersForPackage(context.packageName, null)
        .find { it.provider == componentName }
    }

  Crossfade(state.widgetIds.isEmpty()) { isEmpty ->
    if (isEmpty) {
      HomeScreenNoWidgetsText(
        modifier = modifier.verticalScroll(rememberScrollState()),
        bottom = { Spacer(modifier = Modifier.height(contentPadding.calculateBottomPadding())) },
      )
    } else {
      HomeScreenCountdownsLazyVerticalGrid(modifier = modifier, contentPadding = contentPadding) {
        items(state.widgetIds, key = { widgetId -> "${state.refreshCount}-$widgetId" }) { widgetId
          ->
          AndroidView(
            factory = { ctx -> appWidgetHost.createView(ctx, widgetId, widgetProvider) },
            update = { it.setAppWidget(widgetId, widgetProvider) },
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
          )
        }
      }
    }
  }
}
