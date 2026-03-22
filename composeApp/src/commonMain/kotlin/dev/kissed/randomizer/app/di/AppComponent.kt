package dev.kissed.randomizer.app.di

import dev.kissed.randomizer.app.navigation.AppNavigator
import dev.kissed.randomizer.features.main.presentation.MainScreenViewModel
import dev.kissed.randomizer.features.staffedit.presentation.StaffEditScreenViewModel
import dev.kissed.randomizer.features.stafflist.presentation.StaffScreenViewModel

interface AppComponent {
    val appNavigator: AppNavigator
    fun mainScreenViewModel(): MainScreenViewModel
    fun staffScreenViewModel(): StaffScreenViewModel
    val staffEditScreenViewModelFactory: StaffEditScreenViewModel.Factory
}