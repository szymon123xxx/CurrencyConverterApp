package com.example.currencyconverterapp.core.provider

import kotlinx.coroutines.CoroutineDispatcher

@Suppress("PropertyName")
interface DispatcherProvider {
    val MAIN : CoroutineDispatcher
    val IO : CoroutineDispatcher
    val UNCONFINED : CoroutineDispatcher
    val DEFAULT : CoroutineDispatcher
}