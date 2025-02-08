package com.example.currencyconverterapp.data.network

import retrofit2.http.GET
import retrofit2.http.Path

fun interface ExchangeRateApi {

    @GET("list/{access_key}")
    suspend fun getSupportedCurrencies(@Path("access_key") id: String): SupportedCurrenciesResponse
}