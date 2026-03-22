package dev.kissed.randomizer.app.di

import com.russhwolf.settings.Settings
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Scope

@Scope
annotation class AppComponentScope

@Component
@AppComponentScope
internal abstract class AppComponentImpl : AppComponent {

    @Provides
    protected fun settings(): Settings {
        return Settings()
    }
}
