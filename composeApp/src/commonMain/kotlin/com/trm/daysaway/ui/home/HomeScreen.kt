@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.trm.daysaway.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trm.daysaway.core.base.platformContext
import daysaway.composeapp.generated.resources.Res
import daysaway.composeapp.generated.resources.add_countdown
import daysaway.composeapp.generated.resources.home_screen_title
import daysaway.composeapp.generated.resources.refresh
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(state: HomeScreenState, onAddWidgetClick: () -> Unit) {
  val context = platformContext()

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(stringResource(Res.string.home_screen_title)) },
        actions = {
          IconButton(onClick = { state.refresh(context) }) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = stringResource(Res.string.refresh),
            )
          }
        },
      )
    },
    floatingActionButtonPosition = FabPosition.Center,
    floatingActionButton = {
      LargeExtendedFloatingActionButton(
        onClick = onAddWidgetClick,
        icon = { Icon(Icons.Default.Add, contentDescription = null) },
        text = { Text(text = stringResource(Res.string.add_countdown)) },
      )
    },
  ) { contentPadding ->
    HomeScreenCountdowns(
      state = state,
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 112.dp),
      modifier = Modifier.fillMaxSize().padding(contentPadding),
    )
  }
}
