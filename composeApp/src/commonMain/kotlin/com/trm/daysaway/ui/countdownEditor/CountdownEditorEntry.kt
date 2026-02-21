package com.trm.daysaway.ui.countdownEditor

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.trm.daysaway.domain.Countdown

@Composable
expect fun EntryProviderScope<NavKey>.CountdownEditorEntry(
  onConfirmClick: (Countdown) -> Unit,
  navigateBack: () -> Unit,
)
