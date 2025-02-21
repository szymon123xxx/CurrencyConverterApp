package com.example.currencyconverterapp.data.network

import com.example.currencyconverterapp.data.network.response.CurrenciesConversionResponse
import com.example.currencyconverterapp.data.network.response.SupportedCurrenciesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {

    @GET("list")
    suspend fun getSupportedCurrencies(@Query("access_key") accessKey: String): SupportedCurrenciesResponse

    @GET("convert")
    suspend fun getCurrenciesConversion(
        @Query("access_key") accessKey: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double,
    ): CurrenciesConversionResponse
}