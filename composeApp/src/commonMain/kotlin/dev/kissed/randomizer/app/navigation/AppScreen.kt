package dev.kissed.randomizer.app.navigation

import androidx.navigation3.runtime.NavKey
import dev.kissed.randomizer.model.Member
import kotlinx.serialization.Serializable

sealed interface AppScreen : NavKey {
    @Serializable
    data object Main : AppScreen

    @Serializable
    data object Staff : AppScreen

    @Serializable
    data class StaffEdit(val member: Member?) : AppScreen
}
