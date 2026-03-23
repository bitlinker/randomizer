package dev.kissed.randomizer.app.di

import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Scope

@Scope
annotation class AppComponentScope

@Component
@AppComponentScope
internal abstract class AppComponentImpl(
    @get:Provides val sqlDelightDriverFactory: SqlDelightDriverFactory,
) : AppComponent
