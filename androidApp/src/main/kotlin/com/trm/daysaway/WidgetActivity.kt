package com.trm.daysaway

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.trm.daysaway.core.base.util.showWidgetPinnedToast
import com.trm.daysaway.ui.widget.WidgetScreen

class WidgetActivity : ComponentActivity() {
  @OptIn(ExperimentalMaterial3ExpressiveApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setResult(RESULT_CANCELED)

    val widgetId =
      intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) finish()

    setContent {
      MaterialExpressiveTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
      ) {
        WidgetScreen(
          onConfirmClick = {
            ((application as DaysAwayApp).widgetManager).updateCountdownWidget(widgetId, it)
            showWidgetPinnedToast()
            setResult(RESULT_OK, Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId))
            finish()
          },
          navigateBack = ::finish,
        )
      }
    }
  }
}
