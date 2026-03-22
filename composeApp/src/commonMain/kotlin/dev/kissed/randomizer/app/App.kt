package dev.kissed.randomizer.app

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.GradientFlow
import dev.kissed.randomizer.app.di.AppComponent
import dev.kissed.randomizer.app.navigation.AppScreen
import dev.kissed.randomizer.features.main.presentation.MainScreenComposable
import dev.kissed.randomizer.features.staffedit.presentation.StaffEditScreenComposable
import dev.kissed.randomizer.features.stafflist.presentation.StaffScreenComposable

@Composable
fun App(appComponent: AppComponent) {

    val navigator = remember { appComponent.appNavigator }
    val backStack by navigator.stack.collectAsState()

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shaderBackground(GradientFlow)
                .background(Color.White.copy(alpha = 0.5F)),
        ) {

            NavDisplay(
                backStack = backStack,
                onBack = { navigator.pop() },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { -it })
                },
                popTransitionSpec = {
                    slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                },
                predictivePopTransitionSpec = {
                    slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                },
                entryProvider = { key ->
                    when (key) {
                        is AppScreen.Main -> NavEntry(key) {
                            val viewModel = viewModel { appComponent.mainScreenViewModel() }
                            MainScreenComposable(viewModel)
                        }

                        is AppScreen.Staff -> NavEntry(key) {
                            val viewModel = viewModel { appComponent.staffScreenViewModel() }
                            StaffScreenComposable(viewModel)
                        }

                        is AppScreen.StaffEdit -> NavEntry(key) {
                            val viewModel = viewModel {
                                appComponent.staffEditScreenViewModelFactory.create(key.member)
                            }
                            StaffEditScreenComposable(viewModel)
                        }
                    }
                }
            )
        }
    }
}