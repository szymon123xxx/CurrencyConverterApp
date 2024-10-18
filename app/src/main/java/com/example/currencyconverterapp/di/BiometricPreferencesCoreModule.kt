package com.example.currencyconverterapp.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.currencyconverterapp.domain.BiometricPreferencesUseCase
import com.example.currencyconverterapp.data.BiometricPreferencesUseCaseImpl
import com.example.currencyconverterapp.data.BiometricPreferencesUseCaseImpl.PreferencesKey.PREF_BIOMETRIC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BiometricPreferencesCoreModule {

    @Provides
    fun provideUserDatastore(@ApplicationContext context: Context): BiometricPreferencesUseCase =
        BiometricPreferencesUseCaseImpl(
            PreferenceDataStoreFactory.create(
                produceFile = {
                    context.preferencesDataStoreFile(PREF_BIOMETRIC)
                }
            )
        )
}