package org.example.krpc.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath
import org.example.krpc.models.responses.UserResponse

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

internal const val dataStoreFileName = "example.preferences_pb"

object PreferencesKeys {
    val AUTH_TOKEN = stringPreferencesKey("auth_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
}

expect fun createDataStore(context: Any? = null): DataStore<Preferences>

suspend fun DataStore<Preferences>.getToken() = data
    .map { preferences ->
        val token = preferences[PreferencesKeys.AUTH_TOKEN].orEmpty()
        token
    }.first()

suspend fun DataStore<Preferences>.getRefreshToken() = data
    .map { preferences ->
        val token = preferences[PreferencesKeys.REFRESH_TOKEN].orEmpty()
        token
    }.first()

suspend fun DataStore<Preferences>.saveTokens(userResponse: UserResponse?) {
    edit { preferences ->
        preferences[PreferencesKeys.AUTH_TOKEN] = userResponse?.authToken.orEmpty()
        preferences[PreferencesKeys.REFRESH_TOKEN] = userResponse?.refreshToken.orEmpty()
    }
}