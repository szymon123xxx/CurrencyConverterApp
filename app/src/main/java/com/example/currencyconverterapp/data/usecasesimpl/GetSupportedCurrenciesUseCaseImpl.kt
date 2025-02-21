package com.example.currencyconverterapp.data.usecasesimpl

import android.util.Log
import com.example.currencyconverterapp.BuildConfig
import com.example.currencyconverterapp.core.provider.DispatcherProvider
import com.example.currencyconverterapp.data.network.ExchangeRateApi
import com.example.currencyconverterapp.data.usecasesimpl.GetCurrenciesConversionUseCaseImpl.Companion.CURRENCIES_CONVERSION_TAG
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
            val supportedCurrenciesFail =
                { SupportedCurrenciesResult.Fail("Something went wrong, please try again later.") }

            runCatching {
                val currencies = api.getSupportedCurrencies(BuildConfig.API_KEY)

                currencies.success.takeIf { it }?.let {
                    SupportedCurrenciesResult.Success(currencies.currencies.map { it.key })
                } ?: run {
                    Log.e(CURRENCIES_CONVERSION_TAG, "GetSupportedCurrencies fail result: ${currencies.error}")
                    supportedCurrenciesFail()
                }
            }.getOrElse {
                //For Example IOException, UnknownHostException, SocketTimeoutException
                Log.e(SUPPORTED_CURRENCIES_TAG, "GetSupportedCurrencies exception: $it")
                supportedCurrenciesFail()
            }
        }

    companion object {
        const val SUPPORTED_CURRENCIES_TAG = "SupportedCurrenciesTag"
    }
}