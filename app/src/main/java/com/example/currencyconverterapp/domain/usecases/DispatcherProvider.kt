package com.example.currencyconverterapp.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher

@Suppress("PropertyName")
interface DispatcherProvider {
    val MAIN : CoroutineDispatcher
    val IO : CoroutineDispatcher
    val UNCONFINED : CoroutineDispatcher
    val DEFAULT : CoroutineDispatcher
}