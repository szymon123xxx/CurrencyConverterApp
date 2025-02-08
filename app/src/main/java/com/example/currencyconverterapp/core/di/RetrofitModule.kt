package com.example.currencyconverterapp.core.di

import com.example.currencyconverterapp.BuildConfig
import com.example.currencyconverterapp.data.network.ExchangeRateApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
     return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ExchangeRateApi  = retrofit.create(ExchangeRateApi::class.java)
}