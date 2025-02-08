package com.example.currencyconverterapp.domain.usecases

import androidx.datastore.preferences.core.Preferences

interface DataStorePreferencesUseCase {
    suspend fun getEmail(): String?

    suspend fun setEmail(userName: String): Preferences

    suspend fun getPassword(): String?

    suspend fun setPassword(password: String): Preferences

    suspend fun getUsername(): String?

    suspend fun setUsername(password: String): Preferences
}