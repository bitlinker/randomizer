package dev.kissed.randomizer.features.staffedit.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kissed.randomizer.app.navigation.AppNavigator
import dev.kissed.randomizer.common.ui.Palette
import dev.kissed.randomizer.data.StaffRepository
import dev.kissed.randomizer.model.Member
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.AssistedFactory
import me.tatarka.inject.annotations.Inject

sealed interface StaffEditScreenAction {
    data object ApplyClicked : StaffEditScreenAction
    data object BackClicked : StaffEditScreenAction
    data object DeleteClicked : StaffEditScreenAction
    data class NameChanged(val name: String) : StaffEditScreenAction
    data class EnabledChanged(val isEnabled: Boolean) : StaffEditScreenAction
    data class ColorChanged(val colorInt: Int) : StaffEditScreenAction
}

typealias StaffEditScreenDispatcher = (action: StaffEditScreenAction) -> Unit

@Immutable
data class StaffEditScreenViewState(
    val isApplyEnabled: Boolean,
    val isDeleteVisible: Boolean,
    val item: Member,
)

class StaffEditScreenViewModel @Inject constructor(
    @Assisted private val initialItem: Member?,
    private val staffRepository: StaffRepository,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val mutableStates = MutableStateFlow(
        run {
            val item = initialItem ?: Member(
                id = Member.EMPTY_ID,
                name = "",
                colorInt = Palette.colors.random().toArgb(),
                isEnabled = true,
            )
            StaffEditScreenViewState(
                isApplyEnabled = item.name.isNotBlank(),
                isDeleteVisible = item.id != Member.EMPTY_ID,
                item = item,
            )
        }
    )

    val states: StateFlow<StaffEditScreenViewState> = mutableStates.asStateFlow()

    fun dispatch(action: StaffEditScreenAction) {
        mutableStates.value = reduce(action, mutableStates.value)

        when (action) {
            is StaffEditScreenAction.BackClicked -> {
                appNavigator.pop()
            }

            is StaffEditScreenAction.ApplyClicked -> {
                viewModelScope.launch {
                    staffRepository.addOrUpdate(mutableStates.value.item)
                    appNavigator.pop()
                }
            }

            is StaffEditScreenAction.DeleteClicked -> {
                viewModelScope.launch {
                    staffRepository.delete(mutableStates.value.item)
                    appNavigator.pop()
                }
            }

            else -> Unit
        }
    }

    private fun reduce(
        action: StaffEditScreenAction,
        curState: StaffEditScreenViewState
    ): StaffEditScreenViewState {
        return curState.copy(
            isApplyEnabled = reduceIsApplyEnabled(action, curState.isApplyEnabled),
            item = reduceItem(action, curState.item)
        )
    }

    private fun reduceItem(
        action: StaffEditScreenAction,
        item: Member
    ): Member {
        return item.copy(
            name = reduceItemName(action, item.name),
            colorInt = reduceItemColor(action, item.colorInt),
            isEnabled = reduceItemEnabled(action, item.isEnabled),
        )
    }

    private fun reduceItemEnabled(
        action: StaffEditScreenAction,
        isEnabled: Boolean
    ): Boolean {
        return when (action) {
            is StaffEditScreenAction.EnabledChanged -> action.isEnabled
            else -> isEnabled
        }
    }

    private fun reduceItemColor(
        action: StaffEditScreenAction,
        colorInt: Int
    ): Int {
        return when (action) {
            is StaffEditScreenAction.ColorChanged -> action.colorInt
            else -> colorInt
        }
    }

    private fun reduceItemName(
        action: StaffEditScreenAction,
        name: String
    ): String {
        return when (action) {
            is StaffEditScreenAction.NameChanged -> action.name
            else -> name
        }
    }

    private fun reduceIsApplyEnabled(
        action: StaffEditScreenAction,
        applyEnabled: Boolean
    ): Boolean {
        return when (action) {
            is StaffEditScreenAction.NameChanged -> action.name.isNotBlank()
            else -> applyEnabled
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted initialItem: Member?): StaffEditScreenViewModel
    }
}
