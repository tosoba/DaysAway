package com.trm.daysaway.ui

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.trm.daysaway.core.base.util.popLast
import com.trm.daysaway.core.base.util.pushIfLastNotEqualTo
import com.trm.daysaway.ui.countdownEditor.CountdownEditorEntry
import com.trm.daysaway.ui.home.HomeScreenEntry
import com.trm.daysaway.ui.home.rememberHomeScreenState
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun App(state: AppState) {
  val backStack =
    rememberNavBackStack(
      SavedStateConfiguration {
        serializersModule = SerializersModule {
          polymorphic(NavKey::class) {
            subclass(Home::class, Home.serializer())
            subclass(CountdownEditor::class, CountdownEditor.serializer())
          }
        }
      },
      Home,
    )

  NavDisplay(
    backStack = backStack,
    onBack = backStack::popLast,
    entryProvider =
      entryProvider {
        HomeScreenEntry(
          state = rememberHomeScreenState(appState = state),
          onAddWidgetClick = { backStack.pushIfLastNotEqualTo(CountdownEditor) },
        )

        CountdownEditorEntry(
          onConfirmClick = state.onCountdownConfirmClick,
          navigateBack = backStack::popLast,
        )
      },
  )
}
