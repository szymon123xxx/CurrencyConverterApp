package com.example.currencyconverterapp.data.usecasesimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.currencyconverterapp.domain.usecases.DataStorePreferencesUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStorePreferencesUseCaseImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>
) : DataStorePreferencesUseCase {
    override suspend fun getEmail(): String? = preferencesDataStore.data.first()[KEY_EMAIL]

    override suspend fun setEmail(userName: String) = preferencesDataStore.edit { preferences ->
        preferences[KEY_EMAIL] = userName
    }

    override suspend fun getPassword(): String? = preferencesDataStore.data.first()[KEY_PASSWORD]

    override suspend fun setPassword(password: String) = preferencesDataStore.edit { preferences ->
        preferences[KEY_PASSWORD] = password
    }

    override suspend fun getUsername(): String? =
        preferencesDataStore.data.first()[KEY_USERNAME]

    override suspend fun setUsername(password: String) = preferencesDataStore.edit { preferences ->
        preferences[KEY_USERNAME] = password
    }

    companion object PreferencesKey {
        const val PREF_BIOMETRIC = "biometric_preferences"
        val KEY_EMAIL = stringPreferencesKey("user_email")
        val KEY_PASSWORD = stringPreferencesKey("user_password")
        val KEY_USERNAME = stringPreferencesKey("user_username")
    }
}