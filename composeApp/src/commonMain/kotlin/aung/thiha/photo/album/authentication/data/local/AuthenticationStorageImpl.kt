package aung.thiha.photo.album.authentication.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import aung.thiha.photo.album.authentication.domain.AuthenticationStorage
import aung.thiha.photo.album.authentication.domain.model.AuthenticationSession
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first


class AuthenticationStorageImpl(
    private val dataStore: DataStore<Preferences>,
) : AuthenticationStorage {
    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val USER_ID = stringPreferencesKey("USER_ID")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }

    override suspend fun getAuthenticationSession() : AuthenticationSession? {
        val preferences = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                }
            }
            .first()

        val accessToken = preferences[PreferencesKeys.ACCESS_TOKEN]
        val refreshToken = preferences[PreferencesKeys.REFRESH_TOKEN]
        val userId = preferences[PreferencesKeys.USER_ID]
        return if (accessToken != null && refreshToken != null && userId != null) {
            AuthenticationSession(
                accessToken = accessToken,
                refreshToken = refreshToken,
                userId = userId,
            )
        } else {
            null
        }
    }

    override suspend fun setAuthenticationSession(authenticationSession: AuthenticationSession?) {
        Logger.d("Preference accessToken: ${authenticationSession?.accessToken ?: "nothing"}")
        try {
            dataStore.edit { mutablePreferences ->
                Logger.d("Preference mutablePreferences retrieved")
                authenticationSession?.let { session ->
                    mutablePreferences[PreferencesKeys.ACCESS_TOKEN] = session.accessToken
                    mutablePreferences[PreferencesKeys.REFRESH_TOKEN] = session.refreshToken
                    mutablePreferences[PreferencesKeys.USER_ID] = session.userId
                    Logger.d("Preference saved successfully")
                } ?: run {
                    mutablePreferences.remove(PreferencesKeys.ACCESS_TOKEN)
                    mutablePreferences.remove(PreferencesKeys.REFRESH_TOKEN)
                    mutablePreferences.remove(PreferencesKeys.USER_ID)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e(e.message ?: "no error message")
        }
    }
}