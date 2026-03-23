package dev.kissed.randomizer.app.di

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

interface SqlDelightDriverFactory {
    suspend fun createDriver(name: String, schema: SqlSchema<QueryResult.AsyncValue<Unit>>): SqlDriver
}