package com.example.currencyconverterapp.ui.screens.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun SignUpRoute(
    navigateBack: () -> Unit,
    navigateToScreen: () -> Unit,
) = SignUpRouteContent(
    navigateBack = navigateBack,
    navigateToScreen = navigateToScreen
)

@Composable
fun SignUpRouteContent(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToScreen: () -> Unit,
) {
    val uiState by viewModel.controller.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.controller) {
        viewModel.controller.effects.onEach { effect ->
            when (effect) {
                is SignUpEffect.SuccessfulRegistrationTriggered -> navigateToScreen()
                is SignUpEffect.BackNavigationTriggered -> navigateBack()
            }
        }.launchIn(this)
    }

    SignUpScreen(
        uiState = uiState,
        onAction = viewModel.controller::dispatch,
    )
}