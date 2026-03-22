package dev.kissed.randomizer.features.main.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.kissed.randomizer.common.ui.AppButton
import dev.kissed.randomizer.common.ui.ColorViewComposable
import dev.kissed.randomizer.common.ui.Dimens
import dev.kissed.randomizer.common.ui.TopBarIconButtonComposable
import dev.kissed.randomizer.common.ui.glitchEffect
import org.jetbrains.compose.resources.stringResource
import randomizer.composeapp.generated.resources.Res
import randomizer.composeapp.generated.resources.screen_main_btn_next
import randomizer.composeapp.generated.resources.screen_main_btn_reset
import randomizer.composeapp.generated.resources.screen_main_btn_start
import randomizer.composeapp.generated.resources.screen_main_list_empty_button_title
import randomizer.composeapp.generated.resources.screen_main_list_empty_title
import randomizer.composeapp.generated.resources.screen_main_title
import randomizer.composeapp.generated.resources.screen_main_title_output

@Composable
fun MainScreenComposable(viewModel: MainScreenViewModel) {
    val viewState by viewModel.viewStates.collectAsState()
    MainScreenContent(
        viewState = viewState,
        dispatcher = viewModel::dispatch
    )
}

@Composable
private fun MainScreenContent(
    viewState: MainScreenViewModel.State,
    dispatcher: MainScreenViewModel.Dispatcher,
) {
    Scaffold(
        topBar = { MainTopBar(dispatcher) },
        backgroundColor = Color.Transparent,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (viewState.itemsList.isEmpty()) {
                EmptyItemsContent(dispatcher)
            } else {
                ItemsContent(viewState, dispatcher)
            }
        }
    }
}

@Composable
private fun ItemsContent(
    viewState: MainScreenViewModel.State,
    dispatcher: MainScreenViewModel.Dispatcher
) {
    Column(
        modifier = Modifier
            .padding(vertical = Dimens.dp16)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimens.dp20),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.dp16)
        ) {
            AppButton(
                text = stringResource(
                    when {
                        viewState.currentPos == null -> Res.string.screen_main_btn_start
                        else -> Res.string.screen_main_btn_next
                    }
                ),
                enabled = (viewState.currentPos ?: 0) < viewState.itemsList.size - 1,
                onClick = { dispatcher(MainScreenViewModel.Action.NextClicked) },
                modifier = Modifier.glitchEffect(
                    isEnabled = viewState.currentPos != null,
                    key = viewState.currentPos,
                    glitchColors = remember {
                        listOf(
                            Color.Cyan,
                            Color.Yellow,
                            Color.Magenta
                        )
                    }
                )
            )
            AppButton(
                text = stringResource(Res.string.screen_main_btn_reset),
                onClick = { dispatcher(MainScreenViewModel.Action.ResetClicked) },
            )
        }

        Box(Modifier.fillMaxHeight(fraction = 0.5F)) {
            FortuneWheelPageUI(
                items = viewState.itemsList.filterNot { it.id in viewState.itemsHidden },
                currentId = viewState.currentId,
                onNextTrigger = {
                    dispatcher(MainScreenViewModel.Action.NextClicked)
                },
                onNextAnimationFinished = {
                    dispatcher(MainScreenViewModel.Action.NextAnimationFinished)
                }
            )
        }

        Text(stringResource(Res.string.screen_main_title_output), fontWeight = FontWeight.ExtraBold)
        Column(
            modifier = Modifier.widthIn(min = Dimens.dp150),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(Dimens.dp8)
        ) {
            viewState.chosen.forEachIndexed { idx, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.dp8),
                ) {
                    ColorViewComposable(color = Color(item.colorInt))
                    Text("$idx: ${item.name}")
                }
            }
        }
    }
}

@Composable
private fun EmptyItemsContent(dispatcher: MainScreenViewModel.Dispatcher) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.dp8),
    ) {
        Text(stringResource(Res.string.screen_main_list_empty_title))
        AppButton(
            text = stringResource(Res.string.screen_main_list_empty_button_title),
            onClick = { dispatcher(MainScreenViewModel.Action.SettingsClicked) }
        )
    }
}

@Composable
private fun MainTopBar(dispatcher: MainScreenViewModel.Dispatcher) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.screen_main_title),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        actions = {
            TopBarIconButtonComposable(
                painter = rememberVectorPainter(Icons.Default.Settings),
                onClick = { dispatcher(MainScreenViewModel.Action.SettingsClicked) }
            )
        }
    )
}
