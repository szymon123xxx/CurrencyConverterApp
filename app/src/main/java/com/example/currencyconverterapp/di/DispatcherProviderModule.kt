package com.example.currencyconverterapp.di

import com.example.currencyconverterapp.data.DispatcherProviderImpl
import com.example.currencyconverterapp.domain.DispatcherProvider
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