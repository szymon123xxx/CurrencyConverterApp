package com.example.currencyconverterapp.data.usecasesimpl

import android.util.Log
import com.example.currencyconverterapp.BuildConfig
import com.example.currencyconverterapp.data.network.ExchangeRateApi
import com.example.currencyconverterapp.domain.usecases.DispatcherProvider
import com.example.currencyconverterapp.domain.usecases.GetSupportedCurrenciesUseCase
import com.example.currencyconverterapp.domain.usecases.SupportedCurrenciesResult
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSupportedCurrenciesUseCaseImpl @Inject constructor(
    val dispatcher: DispatcherProvider,
    val api: ExchangeRateApi,
) : GetSupportedCurrenciesUseCase {
    override suspend fun getSupportedCurrencies(): SupportedCurrenciesResult =
        withContext(dispatcher.IO) {
            runCatching {
                val currencies = api.getSupportedCurrencies(BuildConfig.API_KEY)
                SupportedCurrenciesResult.Success(currencies.currencies.map { it.key })
            }.getOrElse {
                //For Example IOException, UnknownHostException, SocketTimeoutException
                Log.e(SUPPORTED_CURRENCIES_TAG, "GetSupportedCurrencies error: $it")
                SupportedCurrenciesResult.Fail("Something went wrong, please try again later.")
            }
        }

    companion object {
        const val SUPPORTED_CURRENCIES_TAG = "SupportedCurrenciesTag"
    }
}