package com.example.currencyconverterapp.domain

import androidx.datastore.preferences.core.Preferences

interface BiometricPreferencesUseCase {
    suspend fun setToken(token: String): Preferences

    suspend fun getEmail(): String?

    suspend fun setEmail(userName: String): Preferences

    suspend fun getPassword(): String?

    suspend fun setPassword(password: String): Preferences

    suspend fun getUsername(): String?

    suspend fun setUsername(password: String): Preferences

    suspend fun isBiometricEnabled(): Boolean

    suspend fun setBiometricEnabled(isEnabled: Boolean): Preferences
}