package com.trm.daysaway.ui.countdownEditor

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.trm.daysaway.domain.Countdown
import com.trm.daysaway.ui.CountdownEditor

@Composable
actual fun EntryProviderScope<NavKey>.CountdownEditorEntry(
  onConfirmClick: (Countdown) -> Unit,
  navigateBack: () -> Unit,
) {
  entry<CountdownEditor> {
    CountdownEditorScreen(
      onConfirmClick = {
        onConfirmClick(it)
        navigateBack()
      },
      navigateBack = navigateBack,
    )
  }
}
