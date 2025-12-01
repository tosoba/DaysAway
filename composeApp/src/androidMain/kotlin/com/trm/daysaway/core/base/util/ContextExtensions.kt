package com.trm.daysaway.core.base.util

import android.content.Context
import android.content.Intent
import android.widget.Toast

inline fun <reified T> Context.actionIntent(action: String): Intent =
  Intent(this, T::class.java).also { it.action = action }

fun Context.showWidgetPinnedToast() {
  Toast.makeText(this, "Widget pinned. Check it on your home screen.", Toast.LENGTH_SHORT).show()
}
