package com.example.currencyconverterapp.di

import android.content.Context
import com.example.currencyconverterapp.cryptomanager.CryptoManagerUseCase
import com.example.currencyconverterapp.cryptomanager.CryptoManagerUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
object CryptoManagerCoreModule {
    @Provides
    fun provideCryptoManagerUseCase(
        @ApplicationContext context: Context
    ): CryptoManagerUseCase = CryptoManagerUseCaseImpl(context)
}