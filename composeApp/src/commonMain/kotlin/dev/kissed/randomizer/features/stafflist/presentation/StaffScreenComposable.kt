package dev.kissed.randomizer.features.stafflist.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.kissed.randomizer.common.ui.ColorViewComposable
import dev.kissed.randomizer.common.ui.Dimens
import dev.kissed.randomizer.common.ui.TopBarIconButtonComposable
import dev.kissed.randomizer.model.Member
import org.jetbrains.compose.resources.stringResource
import randomizer.composeapp.generated.resources.Res
import randomizer.composeapp.generated.resources.screen_staff_title

@Composable
fun StaffScreenComposable(viewModel: StaffScreenViewModel) {
    val viewState by viewModel.viewStates.collectAsState()
    StaffScreenContent(
        viewState = viewState,
        dispatcher = viewModel::dispatch
    )
}

@Composable
private fun StaffScreenContent(
    viewState: StaffScreenViewState,
    dispatcher: StaffScreenDispatcher
) {
    Scaffold(
        topBar = { StaffTopBar(dispatcher) },
        backgroundColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { dispatcher(StaffScreenAction.AddClicked) },
                content = { Icon(Icons.Default.Add, contentDescription = null) }
            )
        }
    ) { paddingValues ->
        StaffList(
            paddingValues = paddingValues,
            items = viewState.items,
            dispatcher = dispatcher,
        )
    }
}

@Composable
private fun StaffTopBar(dispatcher: StaffScreenDispatcher) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.screen_staff_title),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        navigationIcon = {
            TopBarIconButtonComposable(
                painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
                onClick = { dispatcher(StaffScreenAction.BackClicked) }
            )
        }
    )
}

@Composable
private fun StaffList(
    paddingValues: PaddingValues,
    items: List<Member>,
    dispatcher: StaffScreenDispatcher,
) {
    LazyColumn(contentPadding = paddingValues) {
        items(
            items = items,
            key = { item -> item.id },
        ) { item ->
            StaffItem(
                item = item,
                dispatcher = dispatcher,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun StaffItem(
    item: Member,
    dispatcher: StaffScreenDispatcher,
    showCheckbox: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(start = Dimens.dp16, end = Dimens.dp16, top = Dimens.dp16, bottom = 0.dp)
            .defaultMinSize(minHeight = Dimens.dp80)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.dp8),
            modifier = Modifier
                .clickable(
                    onClick = { dispatcher(StaffScreenAction.ItemClicked(item)) },
                )
                .padding(horizontal = Dimens.dp8)
        ) {
            if (showCheckbox) {
                Checkbox(
                    checked = item.isEnabled,
                    onCheckedChange = { dispatcher(StaffScreenAction.ItemEnabledClicked(item)) }
                )
            }
            ColorViewComposable(color = Color(item.colorInt))
            Text(item.name)
        }
    }
}
