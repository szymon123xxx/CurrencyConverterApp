package com.example.currencyconverterapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschuster.control.EffectController
import at.florianschuster.control.createEffectController
import com.example.currencyconverterapp.domain.usecases.CurrenciesConversionResult
import com.example.currencyconverterapp.domain.usecases.GetCurrenciesConversionUseCase
import com.example.currencyconverterapp.domain.usecases.GetSupportedCurrenciesUseCase
import com.example.currencyconverterapp.domain.usecases.SupportedCurrenciesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

typealias HomeController = EffectController<HomeAction, HomeState, HomeEffect>

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    currenciesUseCase: GetSupportedCurrenciesUseCase,
    conversionUseCase: GetCurrenciesConversionUseCase,
) : ViewModel() {

    val controller = viewModelScope.createHomeScreenController(
        initialHomeScreenState = HomeState(),
        currenciesUseCase = currenciesUseCase,
        conversionUseCase = conversionUseCase
    )
}

data class HomeState(
    val acronyms: List<String> = emptyList<String>(),
    val convertFrom: String = "",
    val convertTo: String = "",
    val calculatedValue: Double = 0.0,
    val amountToConvert: String = "",
    val workState: WorkState = WorkState.Loading,
)

interface WorkState {
    data class Error(val message: String) : WorkState
    data object Loading : WorkState
    data object Success : WorkState
}

sealed interface HomeAction {
    data class SetValueInput(val value: String): HomeAction
    data class SetConvertFrom(val from: String) : HomeAction
    data class SetConvertTo(val to: String) : HomeAction
    data object SetValueToCovert : HomeAction
    data object CloseScreen : HomeAction
}

private sealed interface HomeMutation {
    data class UpdateAmountToConvert(val value: String): HomeMutation
    data class UpdateConvertFrom(val from: String) : HomeMutation
    data class UpdateConvertTo(val to: String) : HomeMutation
    data class UpdateAcronyms(val acronyms: List<String>) : HomeMutation
    data class UpdateCalculatedValue(val value: Double): HomeMutation
    data class UpdateError(val message: String) : HomeMutation
}

sealed interface HomeEffect {
    data object Logout : HomeEffect
}

private fun CoroutineScope.createHomeScreenController(
    initialHomeScreenState: HomeState,
    currenciesUseCase: GetSupportedCurrenciesUseCase,
    conversionUseCase: GetCurrenciesConversionUseCase
): HomeController = createEffectController(
    initialState = initialHomeScreenState,
    mutator = { action ->
        when (action) {
            is HomeAction.SetConvertFrom -> flowOf(HomeMutation.UpdateConvertFrom(action.from))

            is HomeAction.SetConvertTo -> flowOf(HomeMutation.UpdateConvertTo(action.to))

            is HomeAction.SetValueInput -> flowOf(HomeMutation.UpdateAmountToConvert(action.value))

            is HomeAction.SetValueToCovert -> flow {
                when (val data = conversionUseCase.getCurrenciesConversion(
                    from = currentState.convertFrom,
                    to = currentState.convertTo,
                    amount = currentState.amountToConvert.toDouble(),
                )) {
                    is CurrenciesConversionResult.Success -> emit(HomeMutation.UpdateCalculatedValue(data.result))
                    is CurrenciesConversionResult.Fail -> emit(HomeMutation.UpdateError(data.info))
                }
            }

            is HomeAction.CloseScreen -> flow {
                emitEffect(HomeEffect.Logout)
            }
        }
    },
    reducer = { mutation: HomeMutation, previousState ->
        when (mutation) {
            is HomeMutation.UpdateConvertFrom -> previousState.copy(
                convertFrom = mutation.from
            )

            is HomeMutation.UpdateAmountToConvert -> previousState.copy(
                amountToConvert = mutation.value
            )

            is HomeMutation.UpdateCalculatedValue -> previousState.copy(
                calculatedValue = mutation.value
            )

            is HomeMutation.UpdateConvertTo -> previousState.copy(
                convertTo = mutation.to
            )

            is HomeMutation.UpdateAcronyms -> previousState.copy(
                acronyms = mutation.acronyms,
                convertFrom = mutation.acronyms[0],
                convertTo = mutation.acronyms[1],
                workState = WorkState.Success,
            )

            is HomeMutation.UpdateError -> previousState.copy(
                workState = WorkState.Error(mutation.message)
            )
        }
    },
    mutationsTransformer = { mutations: Flow<HomeMutation> ->
        mutations.onStart {
            when (val data = currenciesUseCase.getSupportedCurrencies()) {
                is SupportedCurrenciesResult.Success -> emit(HomeMutation.UpdateAcronyms(data.acronyms))
                is SupportedCurrenciesResult.Fail -> emit(HomeMutation.UpdateError(data.info))
            }
        }
    }
)