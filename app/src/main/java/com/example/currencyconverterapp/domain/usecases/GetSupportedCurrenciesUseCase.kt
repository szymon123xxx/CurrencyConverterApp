package com.example.currencyconverterapp.domain.usecases

fun interface GetSupportedCurrenciesUseCase {
    suspend fun getSupportedCurrencies() : SupportedCurrenciesResult
}

sealed interface SupportedCurrenciesResult {
    data class Success(val acronyms: List<String>): SupportedCurrenciesResult
    data class Fail(val info: String): SupportedCurrenciesResult
}
