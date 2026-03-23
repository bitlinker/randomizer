@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.kissed.randomizer.app.di

import app.cash.sqldelight.async.coroutines.await
import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.async.coroutines.awaitMigrate
import app.cash.sqldelight.async.coroutines.awaitQuery
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import dev.kissed.randomizer.Database
import kotlinx.coroutines.DelicateCoroutinesApi
import org.w3c.dom.Worker

class WasmDatabaseFactory : SqlDelightDriverFactory {
    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun createDriver(
        name: String,
        schema: SqlSchema<QueryResult.AsyncValue<Unit>>
    ): SqlDriver {
        return WebWorkerDriver(jsWorker().also { worker ->
            worker.postMessage(name.toJsString())
        }).also { driver ->
            migrateOrCreateIfNeeded(driver)
        }
    }

    private suspend fun migrateOrCreateIfNeeded(driver: SqlDriver) {
        val oldVersion: Long =
            driver.awaitQuery(null, "PRAGMA $VERSION_PRAGMA", mapper = { cursor ->
                if (cursor.next().await()) {
                    cursor.getLong(0)
                } else {
                    null
                }
            }, 0) ?: 0L

        val newVersion = Database.Schema.version

        if (oldVersion == 0L) {
            Database.Schema.awaitCreate(driver)
            val res = driver.await(null, "PRAGMA $VERSION_PRAGMA=$newVersion", 0)
            print(res)
        } else if (oldVersion < newVersion) {
            Database.Schema.awaitMigrate(driver, oldVersion, newVersion)
            val res = driver.await(null, "PRAGMA $VERSION_PRAGMA=$newVersion", 0)
            print(res)
        }
    }

    companion object {
        private const val VERSION_PRAGMA = "user_version"
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun jsWorker(): Worker =
    js("""new Worker(new URL("sqlite.worker.js", import.meta.url))""")

