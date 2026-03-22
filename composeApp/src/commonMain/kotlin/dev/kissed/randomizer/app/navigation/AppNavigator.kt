package dev.kissed.randomizer.app.navigation

import dev.kissed.randomizer.app.di.AppComponentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Inject

@AppComponentScope
class AppNavigator @Inject constructor() {
    private val mutableStack = MutableStateFlow<List<AppScreen>>(listOf(AppScreen.Main))

    val stack: StateFlow<List<AppScreen>> = mutableStack

    fun push(screen: AppScreen) {
        mutableStack.value += screen
    }

    fun pop() {
        mutableStack.value = mutableStack.value.dropLast(1)
    }
}