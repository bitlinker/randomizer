package dev.kissed.randomizer.features.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kissed.randomizer.app.navigation.AppNavigator
import dev.kissed.randomizer.app.navigation.AppScreen
import dev.kissed.randomizer.data.StaffRepository
import dev.kissed.randomizer.model.Member
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class MainScreenViewModel @Inject constructor(
    private val staffRepository: StaffRepository,
    private val navigator: AppNavigator,
) : ViewModel() {

    typealias Dispatcher = (action: Action) -> Unit

    data class State(
        val itemsList: List<Member>,
        val order: List<Int>,
        val itemsHidden: Set<Int>,
        val currentPos: Int?,
        val currentChosen: Boolean,
    ) {
        private val itemsMap: Map<Int, Member> = itemsList.associateBy { it.id }

        val currentId: Int? = currentPos?.let { order[currentPos] }

        val chosen: List<Member> = currentPos?.let {
            order.take(if (currentChosen) currentPos + 1 else currentPos).map { itemsMap[it]!! }
        } ?: emptyList()

        companion object {
            fun create(itemsList: List<Member>): State {
                return State(
                    itemsList = itemsList,
                    order = itemsList.map { it.id }.shuffled(),
                    itemsHidden = emptySet(),
                    currentPos = null,
                    currentChosen = false,
                )
            }
        }
    }

    sealed interface Action {
        data object NextClicked : Action
        data object ResetClicked : Action
        data object NextAnimationFinished : Action
        data object SettingsClicked : Action
    }

    private val mutableState = MutableStateFlow(State.create(emptyList()))

    init {
        viewModelScope.launch {
            staffRepository.get().collect { items ->
                reduceState {
                    val enabledItems = items.filter { it.isEnabled }
                    State.create(enabledItems)
                }
            }
        }
    }

    val viewStates: StateFlow<State> = mutableState

    fun dispatch(action: Action) {
        when (action) {
            Action.NextClicked -> {
                reduceState { state ->
                    val currentPos = state.currentPos
                    when {
                        state.itemsHidden.size == state.itemsList.size - 1 -> state
                        currentPos == null -> {
                            state.copy(currentPos = 0)
                        }

                        else -> {
                            state.copy(
                                currentPos = currentPos + 1,
                                currentChosen = false,
                                itemsHidden = state.currentId!!.let { state.itemsHidden + it }
                            )
                        }
                    }
                }
            }

            Action.NextAnimationFinished -> {
                reduceState { currentState -> currentState.copy(currentChosen = true) }
            }

            is Action.SettingsClicked -> {
                navigator.push(AppScreen.Staff)
            }

            Action.ResetClicked -> {
                reduceState { currentState -> State.create(currentState.itemsList) }
            }
        }
    }

    private fun reduceState(block: (currentState: State) -> State) {
        mutableState.value = block(mutableState.value)
    }
}
