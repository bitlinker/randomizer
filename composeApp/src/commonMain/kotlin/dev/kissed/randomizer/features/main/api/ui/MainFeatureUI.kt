package dev.kissed.randomizer.features.main.api.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kissed.randomizer.features.main.api.MainFeature
import dev.kissed.randomizer.features.main.impl.ui.pages.FortuneWheelPageUI
import dev.kissed.randomizer.features.main.impl.ui.pages.SimplePageUI
import org.jetbrains.compose.resources.stringResource
import randomizer.composeapp.generated.resources.Res
import randomizer.composeapp.generated.resources.btn_next
import randomizer.composeapp.generated.resources.btn_restore
import randomizer.composeapp.generated.resources.btn_save
import randomizer.composeapp.generated.resources.title_input
import randomizer.composeapp.generated.resources.title_main
import randomizer.composeapp.generated.resources.title_output

@Composable
fun MainFeatureUI(state: MainFeature.State, dispatch: (MainFeature.Action) -> Unit) {
    Column(
        Modifier.fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(Res.string.title_main),
            Modifier
                .padding(top = 50.dp),
            fontSize = 20.sp,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AppButton(text = stringResource(Res.string.btn_restore)) { dispatch(MainFeature.Action.Restore) }
            AppButton(text = stringResource(Res.string.btn_save)) { dispatch(MainFeature.Action.Save) }
            AppButton(text = stringResource(Res.string.btn_next)) { dispatch(MainFeature.Action.Next) }
        }
        AnimatedContent(state.page) {
            Box {
                when (it) {
                    MainFeature.Page.WHEEL -> {
                        FortuneWheelPageUI(
                            state.itemsList.filterNot { it.id in state.itemsHidden },
                            state.currentId,
                            onNextTrigger = {
                                dispatch(MainFeature.Action.Next)
                            },
                            onNextAnimationFinished = {
                                dispatch(MainFeature.Action.NextAnimationFinished)
                            }
                        )
                    }

                    MainFeature.Page.SIMPLE -> {
                        val items = state.itemsList.filterNot { it.id in state.itemsHidden }
                        SimplePageUI(
                            items,
                            currentIdx = items.indexOfFirst { it.id == state.currentId }
                                .takeIf { it >= 0 },
                            onNextAniationFinished = {
                                dispatch(MainFeature.Action.NextAnimationFinished)
                            }
                        )
                    }
                }
            }
        }

        Text(stringResource(Res.string.title_input), fontWeight = FontWeight.ExtraBold)
        var itemsFieldState by remember(state.input) { mutableStateOf(state.input) }
        TextField(
            value = itemsFieldState,
            onValueChange = {
                itemsFieldState = it
                dispatch(MainFeature.Action.InputChanged(it))
            },
        )

        Text(stringResource(Res.string.title_output), fontWeight = FontWeight.ExtraBold)
        Column(
            modifier = Modifier.widthIn(min = 100.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            state.chosen.forEachIndexed { idx, item ->
                Text("$idx: ${item.name}")
            }
        }
    }
}

@Composable
private fun AppButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
    ) {
        Text(text)
    }
}
