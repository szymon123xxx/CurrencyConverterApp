package com.example.currencyconverterapp.data.usecasesimpl

import android.util.Log
import com.example.currencyconverterapp.BuildConfig
import com.example.currencyconverterapp.core.provider.DispatcherProvider
import com.example.currencyconverterapp.data.network.ExchangeRateApi
import com.example.currencyconverterapp.domain.usecases.CurrenciesConversionResult
import com.example.currencyconverterapp.domain.usecases.GetCurrenciesConversionUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCurrenciesConversionUseCaseImpl @Inject constructor(
    val api: ExchangeRateApi,
    val dispatcher: DispatcherProvider,
) : GetCurrenciesConversionUseCase {
    override suspend fun getCurrenciesConversion(
        from: String,
        to: String,
        amount: Double
    ): CurrenciesConversionResult = withContext(dispatcher.IO) {
        val currenciesConversionFail =
            { CurrenciesConversionResult.Fail("Something went wrong, please try again later.") }

        runCatching {
            val conversion = api.getCurrenciesConversion(
                accessKey = BuildConfig.API_KEY,
                from = from,
                to = to,
                amount = amount,
            )
            conversion.success.takeIf { it }?.let {
                CurrenciesConversionResult.Success(conversion.result)
            } ?: run {
                Log.e(CURRENCIES_CONVERSION_TAG, "GetSupportedCurrencies fail result: ${conversion.error}")
                currenciesConversionFail()
            }
        }.getOrElse {
            //For Example IOException, UnknownHostException, SocketTimeoutException
            Log.e(CURRENCIES_CONVERSION_TAG, "GetSupportedCurrencies exception: $it")
            currenciesConversionFail()
        }
    }

    companion object {
        const val CURRENCIES_CONVERSION_TAG = "SupportedCurrenciesTag"
    }
}