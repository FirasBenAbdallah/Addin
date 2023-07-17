package addinn.dev.team.utils.datastore

import addinn.dev.team.App
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object DataStoreUtils {

    suspend fun savePreference(key: String, value: Boolean) {
        val keyPreferences = booleanPreferencesKey(key)
        App.context.dataStore.edit {
            it[keyPreferences] = value
        }
    }

    fun readPreferenceWithoutFlow(key: String, defaultValue: Boolean): Boolean {
        val keyPreferences = booleanPreferencesKey(key)
        return runBlocking {
            App.context.dataStore.data.map {
                it[keyPreferences] ?: defaultValue
            }.first()
        }
    }
}