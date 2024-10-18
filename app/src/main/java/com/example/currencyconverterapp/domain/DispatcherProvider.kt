package com.example.currencyconverterapp.domain

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val MAIN : CoroutineDispatcher
    val IO : CoroutineDispatcher
    val UNCONFINED : CoroutineDispatcher
    val DEFAULT : CoroutineDispatcher
}