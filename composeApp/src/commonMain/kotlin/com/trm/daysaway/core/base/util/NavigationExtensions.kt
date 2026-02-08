package com.trm.daysaway.core.base.util

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.popLast() {
  if (size > 1) removeLastOrNull()
}

fun NavBackStack<NavKey>.pushIfLastNotEqualTo(key: NavKey) {
  if (lastOrNull() != key) add(key)
}
