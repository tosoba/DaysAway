package com.trm.daysaway.ui.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.trm.daysaway.core.base.util.getLastWidgetId
import com.trm.daysaway.core.base.util.showWidgetPinnedToast
import com.trm.daysaway.widget.countdown.CountdownWidgetReceiver
import com.trm.daysaway.widget.countdown.toCountdown
import com.trm.daysaway.widget.countdown.updateCountdownWidgetIntent

@Composable
actual fun WidgetScreenPinSuccessEffect(action: () -> Unit) {
  val context = LocalContext.current

  DisposableEffect(Unit) {
    val receiver =
      object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
          context.sendBroadcast(
            context.updateCountdownWidgetIntent(
              widgetId =
                context.getLastWidgetId<CountdownWidgetReceiver>()
                  ?: throw IllegalArgumentException(
                    "Did not find any widget ids for CountdownWidget."
                  ),
              countdown =
                requireNotNull(intent.extras) {
                    "Extras were not provided to CountdownWidgetPinnedReceiver."
                  }
                  .toCountdown(),
            )
          )
          context.showWidgetPinnedToast()
          action()
        }
      }
    ContextCompat.registerReceiver(
      context,
      receiver,
      IntentFilter(WIDGET_PIN_SUCCESS_ACTION),
      ContextCompat.RECEIVER_EXPORTED,
    )

    onDispose { context.unregisterReceiver(receiver) }
  }
}

const val WIDGET_PIN_SUCCESS_ACTION = "WIDGET_PIN_SUCCESS_ACTION"
