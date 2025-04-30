package dev.kissed.randomizer.features.main.api

import dev.kissed.common.architecture.BaseFeature
import dev.kissed.randomizer.features.main.impl.MainFeatureImpl
import dev.kissed.randomizer.features.main.impl.data.InputRepository
import dev.kissed.randomizer.model.Member

interface MainFeature : BaseFeature<MainFeature.State, MainFeature.Action> {
    
    enum class Page {
        WHEEL,
        SIMPLE,
    }
    
    data class State(
        val itemsList: List<Member>,
        val input: String,
        val order: List<Int>,
        val itemsHidden: Set<Int>,
        val currentPos: Int?,
        val currentChosen: Boolean,
        val page: Page,
    ) {
        private val itemsMap: Map<Int, Member> = itemsList.associateBy { it.id }
        
        val currentId: Int? = currentPos?.let { order[currentPos] }
        val current: Member? = currentId?.let { itemsMap[currentId] }

        val chosen: List<Member> = currentPos?.let {
            order.take(if (currentChosen) currentPos + 1 else currentPos).map { itemsMap[it]!! }
        } ?: emptyList()
    }
    
    sealed interface Action {
        data object Next : Action
        data object NextAnimationFinished : Action
        data class InputChanged(val text: String) : Action
        data object Save: Action
        data object Restore: Action
    }
    
    companion object {
        
        fun createImpl(inputRepository: InputRepository): MainFeature {
            return MainFeatureImpl(inputRepository)
        }
    }
}