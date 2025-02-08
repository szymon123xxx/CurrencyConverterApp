package com.example.currencyconverterapp.data.network

data class SupportedCurrenciesResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val currencies: Map<String, String>,
)
