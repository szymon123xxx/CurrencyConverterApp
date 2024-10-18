package com.example.currencyconverterapp.data

import com.example.currencyconverterapp.domain.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DispatcherProviderImpl @Inject constructor(): DispatcherProvider {
    override val DEFAULT: CoroutineDispatcher
        get() = Dispatchers.Default

    override val IO: CoroutineDispatcher
        get() = Dispatchers.IO

    override val MAIN: CoroutineDispatcher
        get() = Dispatchers.Main

    override val UNCONFINED: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}