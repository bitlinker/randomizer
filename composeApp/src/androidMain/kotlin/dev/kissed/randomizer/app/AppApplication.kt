package dev.kissed.randomizer.app

import android.app.Application
import dev.kissed.randomizer.app.di.AndroidDatabaseFactory
import dev.kissed.randomizer.app.di.AppComponent

class AppApplication : Application() {
    internal val appComponent = AppComponent.create(
        sqlDelightDriverFactory = AndroidDatabaseFactory(this)
    )
}
