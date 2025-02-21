package com.example.currencyconverterapp.core.di

import com.example.currencyconverterapp.data.usecasesimpl.GetCurrenciesConversionUseCaseImpl
import com.example.currencyconverterapp.domain.usecases.GetCurrenciesConversionUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
fun interface CurrenciesConversionModule {

    @Binds
    fun bindCurrenciesConversionUseCase(impl: GetCurrenciesConversionUseCaseImpl): GetCurrenciesConversionUseCase
}