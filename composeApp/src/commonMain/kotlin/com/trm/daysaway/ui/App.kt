package com.trm.daysaway.ui

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.trm.daysaway.core.base.util.popLast
import com.trm.daysaway.core.base.util.pushIfLastNotEqualTo
import com.trm.daysaway.ui.countdownEditor.CountdownEditorConfirmationSuccessEffect
import com.trm.daysaway.ui.countdownEditor.CountdownEditorScreen
import com.trm.daysaway.ui.home.HomeScreen
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
        entry<Home> {
          HomeScreen(
            state = rememberHomeScreenState(appState = state),
            onAddWidgetClick = { backStack.pushIfLastNotEqualTo(CountdownEditor) },
          )
        }
        entry<CountdownEditor> {
          CountdownEditorConfirmationSuccessEffect(action = backStack::popLast)

          CountdownEditorScreen(
            onConfirmClick = state.onCountdownConfirmClick,
            navigateBack = backStack::popLast,
          )
        }
      },
  )
}
