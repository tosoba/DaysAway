package com.trm.daysaway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.runtime.rememberCoroutineScope
import com.trm.daysaway.ui.App
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalMaterial3ExpressiveApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContent {
      MaterialExpressiveTheme {
        val scope = rememberCoroutineScope()
        App(
          onWidgetConfirmClick = {
            scope.launch { ((application as DaysAwayApp).widgetManager).addCountdownWidget(it) }
          }
        )
      }
    }
  }
}
