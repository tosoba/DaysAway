package com.trm.daysaway.ui

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.trm.daysaway.core.base.util.popLast
import com.trm.daysaway.core.base.util.pushIfLastNotEqualTo
import com.trm.daysaway.domain.Countdown
import com.trm.daysaway.ui.home.HomeScreen
import com.trm.daysaway.ui.countdownEditor.CountdownEditorScreen
import com.trm.daysaway.ui.countdownEditor.CountdownEditorConfirmationSuccessEffect
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun App(onCountdownConfirmClick: (Countdown) -> Unit) {
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
          HomeScreen(onAddWidgetClick = { backStack.pushIfLastNotEqualTo(CountdownEditor) })
        }
        entry<CountdownEditor> {
          CountdownEditorConfirmationSuccessEffect(action = backStack::popLast)

          CountdownEditorScreen(
            onConfirmClick = onCountdownConfirmClick,
            navigateBack = backStack::popLast,
          )
        }
      },
  )
}
