package dev.kissed.randomizer.features.main.impl.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import dev.kissed.randomizer.app.di.AppComponentScope
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.Scope

@Scope
annotation class InputRepositoryScope

@AppComponentScope
class InputRepository @Inject constructor(
    private val settings: Settings,
) {

    fun saveCurrent(input: String) {
        settings.putString(KEY_INPUT_CURRENT, input)
    }

    fun getCurrent(): String? {
        return settings[KEY_INPUT_CURRENT]
    }

    fun saveDefault(input: String) {
        settings.putString(KEY_INPUT_DEFAULT, input)
    }

    fun getDefault(): String? {
        return settings[KEY_INPUT_DEFAULT]
    }

    companion object {
        private const val KEY_INPUT_CURRENT = "input_current"
        private const val KEY_INPUT_DEFAULT = "input_default"
    }
}