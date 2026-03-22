package dev.kissed.randomizer.features.staffedit.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import dev.kissed.randomizer.common.ui.ColorViewComposable
import dev.kissed.randomizer.common.ui.Dimens
import dev.kissed.randomizer.common.ui.Palette
import dev.kissed.randomizer.common.ui.TopBarIconButtonComposable
import org.jetbrains.compose.resources.stringResource
import randomizer.composeapp.generated.resources.Res
import randomizer.composeapp.generated.resources.screen_staff_edit_color_title
import randomizer.composeapp.generated.resources.screen_staff_edit_enabled_title
import randomizer.composeapp.generated.resources.screen_staff_edit_name_title
import randomizer.composeapp.generated.resources.screen_staff_edit_title

@Composable
fun StaffEditScreenComposable(viewModel: StaffEditScreenViewModel) {
    val state by viewModel.states.collectAsState()
    StaffEditScreenContent(state, viewModel::dispatch)
}

@Composable
private fun StaffEditScreenContent(
    state: StaffEditScreenViewState,
    dispatcher: StaffEditScreenDispatcher,
) {
    Scaffold(
        topBar = {
            StaffEditTopBar(
                isApplyEnabled = state.isApplyEnabled,
                isDeleteVisible = state.isDeleteVisible,
                dispatcher = dispatcher
            )
        },
        backgroundColor = Color.Transparent,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = Dimens.dp16)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.dp8),
        ) {
            Text(stringResource(Res.string.screen_staff_edit_name_title))
            TextField(
                value = state.item.name,
                onValueChange = {
                    dispatcher(StaffEditScreenAction.NameChanged(it))
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(stringResource(Res.string.screen_staff_edit_color_title))
            StaffEditPalette(Color(state.item.colorInt), dispatcher)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.dp8)
            ) {
                Checkbox(
                    checked = state.item.isEnabled,
                    onCheckedChange = { dispatcher(StaffEditScreenAction.EnabledChanged(!state.item.isEnabled)) }
                )
                Text(stringResource(Res.string.screen_staff_edit_enabled_title))
            }
        }
    }
}

@Composable
private fun StaffEditTopBar(
    isApplyEnabled: Boolean,
    isDeleteVisible: Boolean,
    dispatcher: StaffEditScreenDispatcher
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.screen_staff_edit_title),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        navigationIcon = {
            TopBarIconButtonComposable(
                painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
                onClick = { dispatcher(StaffEditScreenAction.BackClicked) }
            )
        },
        actions = {
            if (isDeleteVisible) {
                TopBarIconButtonComposable(
                    painter = rememberVectorPainter(Icons.Default.Delete),
                    onClick = { dispatcher(StaffEditScreenAction.DeleteClicked) }
                )
            }
            TopBarIconButtonComposable(
                painter = rememberVectorPainter(Icons.Default.Check),
                onClick = { dispatcher(StaffEditScreenAction.ApplyClicked) },
                isEnabled = isApplyEnabled,
            )
        }
    )
}

@Composable
private fun StaffEditPalette(
    curColor: Color,
    dispatcher: StaffEditScreenDispatcher
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.dp8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(items = Palette.colors) { color ->
            ColorViewComposable(
                color = color,
                isSelected = curColor == color,
                onClick = { dispatcher(StaffEditScreenAction.ColorChanged(color.toArgb())) }
            )
        }
    }
}
