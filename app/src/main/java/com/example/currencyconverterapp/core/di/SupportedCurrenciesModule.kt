package com.example.currencyconverterapp.core.di

import com.example.currencyconverterapp.data.usecasesimpl.GetSupportedCurrenciesUseCaseImpl
import com.example.currencyconverterapp.domain.usecases.GetSupportedCurrenciesUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
fun interface SupportedCurrenciesModule {

    @Binds
    fun bindCurrenciesUseCase(impl: GetSupportedCurrenciesUseCaseImpl): GetSupportedCurrenciesUseCase
}