package dev.kissed.randomizer.app.di

import android.app.Application
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class AndroidDatabaseFactory(private val application: Application) : SqlDelightDriverFactory {
    override suspend fun createDriver(name: String, schema: SqlSchema<QueryResult.AsyncValue<Unit>>): SqlDriver {
        return AndroidSqliteDriver(schema.synchronous(), application, name)
    }
}