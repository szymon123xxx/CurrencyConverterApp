package com.example.currencyconverterapp.data.network.response

data class ApiErrorResponse(
    val code: String,
    val type: String,
    val info: String,
)
