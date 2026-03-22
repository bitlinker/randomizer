package dev.kissed.randomizer.features.stafflist.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kissed.randomizer.app.navigation.AppNavigator
import dev.kissed.randomizer.app.navigation.AppScreen
import dev.kissed.randomizer.data.StaffRepository
import dev.kissed.randomizer.model.Member
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

sealed interface StaffScreenAction {
    data object AddClicked : StaffScreenAction
    data object BackClicked : StaffScreenAction
    data class ItemClicked(val item: Member) : StaffScreenAction
    data class ItemEnabledClicked(val item: Member) : StaffScreenAction
}

@Immutable
data class StaffScreenViewState(
    val items: List<Member> = emptyList(),
)

typealias StaffScreenDispatcher = (action: StaffScreenAction) -> Unit

class StaffScreenViewModel @Inject constructor(
    private val staffRepository: StaffRepository,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val mutableStates = MutableStateFlow(StaffScreenViewState())

    val viewStates = mutableStates.asStateFlow()

    fun dispatch(action: StaffScreenAction) {
        when (action) {
            is StaffScreenAction.AddClicked -> {
                navigator.push(AppScreen.StaffEdit(null))
            }

            is StaffScreenAction.ItemClicked -> {
                navigator.push(AppScreen.StaffEdit(action.item))
            }

            is StaffScreenAction.BackClicked -> {
                navigator.pop()
            }

            is StaffScreenAction.ItemEnabledClicked -> {
                viewModelScope.launch {
                    staffRepository.addOrUpdate(
                        action.item.copy(
                            isEnabled = !action.item.isEnabled
                        )
                    )
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            staffRepository.get().collect { items ->
                mutableStates.value = mutableStates.value.copy(
                    items = items
                )
            }
        }
    }
}
