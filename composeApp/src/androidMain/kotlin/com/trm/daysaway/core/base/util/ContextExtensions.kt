package com.trm.daysaway.core.base.util

import android.content.Context
import android.content.Intent

inline fun <reified T> Context.actionIntent(action: String): Intent =
  Intent(this, T::class.java).also { it.action = action }
