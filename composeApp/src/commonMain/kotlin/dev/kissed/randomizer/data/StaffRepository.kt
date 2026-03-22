package dev.kissed.randomizer.data

import com.russhwolf.settings.Settings
import dev.kissed.randomizer.app.di.AppComponentScope
import dev.kissed.randomizer.model.Member
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@AppComponentScope
class StaffRepository @Inject constructor(
    private val settings: Settings,
) {
    // TODO: sqldelight

    private val mutableMembers = MutableStateFlow(readSettings() ?: emptyList())

    private val mutex = Mutex()

    fun get(): Flow<List<Member>> {
        return mutableMembers
    }

    suspend fun delete(member: Member) {
        withContext(Dispatchers.Default) {
            mutex.withLock {
                mutableMembers.value = mutableMembers.value
                    .filter { it.id != member.id }
                writeSettings(mutableMembers.value)
            }
        }
    }

    suspend fun addOrUpdate(member: Member) {
        withContext(Dispatchers.Default) {
            mutex.withLock {
                if (member.id == Member.EMPTY_ID) {
                    mutableMembers.value += member.copy(
                        id = (mutableMembers.value.maxOfOrNull { it.id } ?: 0) + 1)
                } else {
                    mutableMembers.value = mutableMembers.value
                        .map { if (it.id == member.id) member else it }
                }
                writeSettings(mutableMembers.value)
            }
        }
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