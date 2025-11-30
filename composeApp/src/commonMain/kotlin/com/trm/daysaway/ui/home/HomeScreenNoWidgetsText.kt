package com.trm.daysaway.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import daysaway.composeapp.generated.resources.Res
import daysaway.composeapp.generated.resources.widgets
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HomeScreenNoWidgetsText(modifier: Modifier = Modifier, bottom: @Composable () -> Unit) {
  val containerDpSize = LocalWindowInfo.current.containerDpSize
  val windowSizeClass =
    remember(containerDpSize) { WindowSizeClass.calculateFromSize(containerDpSize) }

  if (windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact) {
    Column(modifier = modifier) {
      Row(
        modifier = Modifier.fillMaxWidth().weight(1f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Spacer(modifier = Modifier.width(16.dp))

        WidgetsIcon()

        Column(
          modifier = Modifier.padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.Center,
        ) {
          WidgetsText()
        }
      }
      bottom()
    }
  } else {
    Column(
      modifier = modifier,
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      WidgetsIcon()
      WidgetsText()
      bottom()
    }
  }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ColumnScope.WidgetsText() {
  Text(
    "No widgets added yet.",
    style = MaterialTheme.typography.titleLargeEmphasized,
    textAlign = TextAlign.Start,
  )
  Text(
    "Create one using the button below.",
    style = MaterialTheme.typography.bodyMedium,
    textAlign = TextAlign.Start,
  )
}

@Composable
private fun WidgetsIcon() {
  Icon(
    painterResource(Res.drawable.widgets),
    contentDescription = null,
    modifier = Modifier.size(64.dp),
  )
}
