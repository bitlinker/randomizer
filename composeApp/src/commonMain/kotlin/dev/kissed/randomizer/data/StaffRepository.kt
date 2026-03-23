package dev.kissed.randomizer.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.russhwolf.settings.Settings
import dev.kissed.randomizer.Staff
import dev.kissed.randomizer.app.di.AppComponentScope
import dev.kissed.randomizer.model.Member
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@AppComponentScope
class StaffRepository @Inject constructor(
    private val appDatabase: AppDatabase,
) {
    private val settings = Settings()

    init {
        GlobalScope.launch {
            migrateFromSettingsOnce()
        }
    }

    private suspend fun staffQueries() = appDatabase.database().staffQueries

    @OptIn(ExperimentalCoroutinesApi::class)
    fun get(): Flow<List<Member>> {
        return suspend { staffQueries() }
            .asFlow()
            .flatMapLatest { queries ->
                queries.selectAll()
                    .asFlow()
                    .mapToList(Dispatchers.Default)
                    .map { list ->
                        list.map { it.fromEntity() }
                    }
            }
    }

    suspend fun delete(member: Member) {
        require(member.id != Member.EMPTY_ID)
        staffQueries().delete(member.id)
    }

    suspend fun addOrUpdate(member: Member) {
        if (member.id == Member.EMPTY_ID) {
            staffQueries().insert(
                name = member.name,
                color = member.colorInt.toLong(),
                isEnabled = member.isEnabled
            )
        } else {
            staffQueries().update(
                name = member.name,
                color = member.colorInt.toLong(),
                isEnabled = member.isEnabled,
                id = member.id,
            )
        }
    }

    private suspend fun migrateFromSettingsOnce() {
        // Fill for debug
        //writeSettings(listOf(Member(id=555, name = "settings", colorInt = 0, isEnabled = true)))
        readSettings()?.forEach { item ->
            addOrUpdate(item.copy(id = Member.EMPTY_ID))
        }
        writeSettings(emptyList())
    }

    private fun readSettings(): List<Member>? {
        return settings.getStringOrNull(KEY_MEMBERS)?.let { serializedData ->
            runCatching { json.decodeFromString<List<Member>>(serializedData) }
        }?.getOrNull()
    }

    private fun writeSettings(value: List<Member>) {
        val serializedData = json.encodeToString(value)
        settings.putString(KEY_MEMBERS, serializedData)
    }

    companion object {
        private const val KEY_MEMBERS = "KEY_MEMBERS"
        private val json = Json {
            ignoreUnknownKeys = true
        }
    }
}

private fun Staff.fromEntity(): Member {
    return Member(
        id = id,
        name = name,
        colorInt = color.toInt(),
        isEnabled = isEnabled,
    )
}
