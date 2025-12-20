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
import daysaway.composeapp.generated.resources.home_screen_no_widgets_added
import daysaway.composeapp.generated.resources.home_screen_no_widgets_create_one
import daysaway.composeapp.generated.resources.widgets
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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

        Column(verticalArrangement = Arrangement.Center) { WidgetsText(TextAlign.Start) }
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
      WidgetsText(TextAlign.Center)
      bottom()
    }
  }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ColumnScope.WidgetsText(textAlign: TextAlign) {
  Text(
    stringResource(Res.string.home_screen_no_widgets_added),
    style = MaterialTheme.typography.titleLargeEmphasized,
    textAlign = textAlign,
    modifier = Modifier.padding(horizontal = 16.dp),
  )
  Text(
    stringResource(Res.string.home_screen_no_widgets_create_one),
    style = MaterialTheme.typography.bodyMedium,
    textAlign = textAlign,
    modifier = Modifier.padding(horizontal = 16.dp),
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
