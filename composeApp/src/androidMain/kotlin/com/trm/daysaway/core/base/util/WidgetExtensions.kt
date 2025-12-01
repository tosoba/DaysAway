package com.trm.daysaway.core.base.util

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.GlanceStateDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal fun <T> GlanceAppWidget.updateWidget(
  widgetId: Int,
  definition: GlanceStateDefinition<T>,
  context: Context,
  updateState: suspend (T) -> T,
) {
  CoroutineScope(context = SupervisorJob() + Dispatchers.Default).launch {
    val glanceId = context.getGlanceIdByWidgetId(widgetId)
    updateAppWidgetState(
      context = context,
      definition = definition,
      glanceId = glanceId,
      updateState = updateState,
    )
    update(context, glanceId)
  }
}

internal fun Context.getGlanceIdByWidgetId(widgetId: Int): GlanceId =
  GlanceAppWidgetManager(this).getGlanceIdBy(widgetId)

internal inline fun <reified T : GlanceAppWidgetReceiver> Context.getLastWidgetId(): Int? =
  AppWidgetManager.getInstance(this).getAppWidgetIds(widgetReceiverComponentName<T>()).lastOrNull()

internal inline fun <reified T : GlanceAppWidgetReceiver> Context.widgetReceiverComponentName():
  ComponentName = ComponentName(applicationContext.packageName, T::class.java.name)

@Composable
internal fun stringResource(@StringRes id: Int, args: List<Any> = emptyList()): String =
  LocalContext.current.getString(id, *args.toTypedArray())

@Composable
internal fun pluralStringResource(
  @StringRes id: Int,
  quantity: Int,
  args: List<Any> = emptyList(),
): String = LocalContext.current.resources.getQuantityString(id, quantity, *args.toTypedArray())
