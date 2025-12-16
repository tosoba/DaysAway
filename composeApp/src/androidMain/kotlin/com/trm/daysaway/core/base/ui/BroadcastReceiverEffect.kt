package com.trm.daysaway.core.base.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun BroadcastReceiverEffect(
  intentFilter: IntentFilter,
  onReceive: (context: Context, intent: Intent) -> Unit,
) {
  val context = LocalContext.current
  DisposableEffect(context, intentFilter) {
    val receiver =
      object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
          onReceive(context, intent)
        }
      }
    ContextCompat.registerReceiver(context, receiver, intentFilter, ContextCompat.RECEIVER_EXPORTED)

    onDispose { context.unregisterReceiver(receiver) }
  }
}
