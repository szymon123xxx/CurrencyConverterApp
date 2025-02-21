package com.example.currencyconverterapp.domain.usecases

fun interface GetCurrenciesConversionUseCase {
    suspend fun getCurrenciesConversion(
        from: String,
        to: String,
        amount: Double,
    ): CurrenciesConversionResult
}

sealed interface CurrenciesConversionResult {
    data class Success(val result: Double) : CurrenciesConversionResult
    data class Fail(val info: String) : CurrenciesConversionResult
}