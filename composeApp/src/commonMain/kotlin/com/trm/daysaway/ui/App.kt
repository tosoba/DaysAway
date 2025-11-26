@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.trm.daysaway.ui

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.trm.daysaway.core.domain.WidgetManager
import com.trm.daysaway.core.ui.util.popLast
import com.trm.daysaway.core.ui.util.pushIfLastNotEqualTo
import com.trm.daysaway.ui.home.HomeScreen
import com.trm.daysaway.ui.widget.WidgetScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun App(widgetManager: WidgetManager) {
  MaterialExpressiveTheme {
    val backStack =
      rememberNavBackStack(
        SavedStateConfiguration {
          serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
              subclass(Home::class, Home.serializer())
              subclass(Widget::class, Widget.serializer())
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
          entry<Home> { HomeScreen(onAddWidgetClick = { backStack.pushIfLastNotEqualTo(Widget) }) }

          entry<Widget> {
            WidgetScreen(widgetManager = widgetManager, onBackClick = backStack::popLast)
          }
        },
    )
  }
}
