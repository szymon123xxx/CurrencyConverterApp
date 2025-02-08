package com.example.currencyconverterapp.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverterapp.domain.usecases.GetSupportedCurrenciesUseCase
import com.example.currencyconverterapp.domain.usecases.SupportedCurrenciesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    currencies: GetSupportedCurrenciesUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            when (val response = currencies.getSupportedCurrencies()) {
                is SupportedCurrenciesResult.Success -> Log.d("ELOELO", "HEJ SUCESS ${response.acronyms}")
                is SupportedCurrenciesResult.Fail -> Log.d("ELOELO", "HEJ FAIL ${response.info}")
            }
        }
    }
}