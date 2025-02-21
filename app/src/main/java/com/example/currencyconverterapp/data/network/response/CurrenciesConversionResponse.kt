package com.example.currencyconverterapp.data.network.response

data class CurrenciesConversionResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val query: Query,
    val info: Info,
    val result: Double,
    val error: ApiErrorResponse,
)

data class Query(
    val from: String,
    val to: String,
    val amount: Double
)

data class Info(
    val timestamp: Long,
    val quote: Double
)