package com.example.currencyconverterapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.currencyconverterapp.domain.BiometricPreferencesUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class BiometricPreferencesUseCaseImpl @Inject constructor(
    @Named(PREF_BIOMETRIC)
    private val preferencesDataStore: DataStore<Preferences>
): BiometricPreferencesUseCase {
    override suspend fun setToken(token: String) = preferencesDataStore.edit { preferences ->
        preferences[KEY_TOKEN] = token
    }

    override suspend fun getEmail(): String? =
        preferencesDataStore.data.first()[KEY_EMAIL]

    override suspend fun setEmail(userName: String) = preferencesDataStore.edit { preferences ->
        preferences[KEY_EMAIL] = userName
    }

    override suspend fun getPassword(): String? =
        preferencesDataStore.data.first()[KEY_PASSWORD]

    override suspend fun setPassword(password: String) = preferencesDataStore.edit { preferences ->
        preferences[KEY_PASSWORD] = password
    }

    override suspend fun getUsername(): String? =
        preferencesDataStore.data.first()[KEY_USERNAME]

    override suspend fun setUsername(password: String) = preferencesDataStore.edit { preferences ->
        preferences[KEY_USERNAME] = password
    }

    override suspend fun isBiometricEnabled(): Boolean = preferencesDataStore.data.first()[KEY_BIOMETRIC_ENABLED] == true

    override suspend fun setBiometricEnabled(isEnabled: Boolean) = preferencesDataStore.edit { preferences ->
        preferences[KEY_BIOMETRIC_ENABLED] = isEnabled
    }

    companion object PreferencesKey {
        const val PREF_BIOMETRIC = "biometric_preferences"
        const val ENCRYPTED_FILE_NAME = "encrypted_data_store"
        const val SECRET_KEY = "biometric_secret_key"

        val KEY_TOKEN = stringPreferencesKey("user_token")
        val KEY_EMAIL = stringPreferencesKey("user_email")
        val KEY_PASSWORD = stringPreferencesKey("user_password")
        val KEY_USERNAME = stringPreferencesKey("user_username")
        val KEY_BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    }
}