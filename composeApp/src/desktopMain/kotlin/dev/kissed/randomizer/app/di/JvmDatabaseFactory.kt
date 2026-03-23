package dev.kissed.randomizer.app.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.util.Properties

class JvmDatabaseFactory : SqlDelightDriverFactory {
    override suspend fun createDriver(name: String, schema: SqlSchema<QueryResult.AsyncValue<Unit>>): SqlDriver {
        return JdbcSqliteDriver("jdbc:sqlite:$name", Properties(), schema.synchronous())
    }
}