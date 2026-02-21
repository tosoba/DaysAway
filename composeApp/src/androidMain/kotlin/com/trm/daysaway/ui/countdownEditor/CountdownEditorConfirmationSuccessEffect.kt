package com.trm.daysaway.ui.countdownEditor

import android.content.IntentFilter
import androidx.compose.runtime.Composable
import com.trm.daysaway.core.base.util.getLastWidgetId
import com.trm.daysaway.core.base.util.showWidgetPinnedToast
import com.trm.daysaway.core.ui.BroadcastReceiverEffect
import com.trm.daysaway.widget.countdown.CountdownWidgetReceiver
import com.trm.daysaway.widget.countdown.toCountdown
import com.trm.daysaway.widget.countdown.updateCountdownWidgetIntent

@Composable
fun CountdownEditorConfirmationSuccessEffect(action: () -> Unit) {
  BroadcastReceiverEffect(intentFilter = IntentFilter(WIDGET_PIN_SUCCESS_ACTION)) { context, intent
    ->
    context.sendBroadcast(
      context.updateCountdownWidgetIntent(
        widgetId =
          context.getLastWidgetId<CountdownWidgetReceiver>()
            ?: throw IllegalArgumentException("Did not find any widget ids for CountdownWidget."),
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

const val WIDGET_PIN_SUCCESS_ACTION = "WIDGET_PIN_SUCCESS_ACTION"
