@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.trm.daysaway.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import daysaway.composeapp.generated.resources.Res
import daysaway.composeapp.generated.resources.add_widget
import daysaway.composeapp.generated.resources.home_screen_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(onAddWidgetClick: () -> Unit) {
  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(title = { Text(stringResource(Res.string.home_screen_title)) })
    },
    floatingActionButtonPosition = FabPosition.Center,
    floatingActionButton = {
      LargeExtendedFloatingActionButton(onClick = onAddWidgetClick) {
        Text(
          text =
            buildAnnotatedString {
              appendInlineContent(ICON_PLACEHOLDER)
              append(' ')
              append(stringResource(Res.string.add_widget))
            },
          inlineContent =
            mapOf(
              ICON_PLACEHOLDER to
                InlineTextContent(
                  Placeholder(
                    width = MaterialTheme.typography.headlineSmall.fontSize,
                    height = MaterialTheme.typography.headlineSmall.fontSize,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                  )
                ) {
                  Icon(Icons.Default.Add, contentDescription = null)
                }
            ),
        )
      }
    },
  ) { contentPadding ->
    HomeScreenWidgetsGrid(
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 112.dp),
      modifier = Modifier.fillMaxSize().padding(contentPadding),
    )
  }
}

private const val ICON_PLACEHOLDER = "icon"
