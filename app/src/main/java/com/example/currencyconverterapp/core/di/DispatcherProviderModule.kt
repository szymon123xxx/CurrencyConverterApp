package com.example.currencyconverterapp.core.di

import com.example.currencyconverterapp.data.usecasesimpl.DispatcherProviderImpl
import com.example.currencyconverterapp.domain.usecases.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
fun interface DispatcherProviderModule {

    @Singleton
    @Binds
    fun provideDispatcherProviderUseCase(dispatcherProvider: DispatcherProviderImpl) : DispatcherProvider
}