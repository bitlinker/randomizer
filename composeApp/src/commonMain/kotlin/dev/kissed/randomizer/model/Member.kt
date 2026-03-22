package dev.kissed.randomizer.model

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val id: Int,
    val name: String,
    val colorInt: Int,
    val isEnabled: Boolean,
) {
    companion object {
        const val EMPTY_ID = -1
    }
}