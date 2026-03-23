package dev.kissed.randomizer.data

import dev.kissed.randomizer.Database
import dev.kissed.randomizer.app.di.AppComponentScope
import dev.kissed.randomizer.app.di.SqlDelightDriverFactory
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.tatarka.inject.annotations.Inject

@AppComponentScope
class AppDatabase @Inject constructor(
    private val driverFactory: SqlDelightDriverFactory
) {
    private var database: Database? = null
    private val mutex = Mutex()

    suspend fun database(): Database {
        mutex.withLock {
            if (database == null) {
                val driver = driverFactory.createDriver("database.sqlite3", Database.Schema)
                database = Database(driver)
            }
            return database!!
        }
    }
}
