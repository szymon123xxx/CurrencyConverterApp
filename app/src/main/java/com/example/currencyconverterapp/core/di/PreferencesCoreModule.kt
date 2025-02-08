package com.example.currencyconverterapp.core.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.currencyconverterapp.data.usecasesimpl.DataStorePreferencesUseCaseImpl
import com.example.currencyconverterapp.data.usecasesimpl.DataStorePreferencesUseCaseImpl.PreferencesKey.PREF_BIOMETRIC
import com.example.currencyconverterapp.domain.usecases.DataStorePreferencesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesCoreModule {

    @Provides
    @Singleton
    fun provideUserDatastore(@ApplicationContext context: Context): DataStorePreferencesUseCase =
        DataStorePreferencesUseCaseImpl(
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile(PREF_BIOMETRIC) }
            )
        )
}