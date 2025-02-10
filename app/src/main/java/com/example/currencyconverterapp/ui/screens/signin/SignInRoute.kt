package com.example.currencyconverterapp.ui.screens.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

//wrtie some unit test for navigation :)
@Composable
fun SignInRoute(
    navigateBack: () -> Unit,
    navigateToScreen: () -> Unit,
) = SignInRouteContent(
    navigateBack = navigateBack,
    navigateToScreen = navigateToScreen
)

@Composable
fun SignInRouteContent(
    viewModel: SignInViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToScreen: () -> Unit,
) {
    val uiState by viewModel.controller.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.controller) {
        viewModel.controller.effects.onEach { effect ->
            when (effect) {
                is SignInEffect.SuccessfulAuthorizationTriggered -> navigateToScreen()
                is SignInEffect.BackNavigationTriggered -> navigateBack()
            }
        }.launchIn(this)
    }

    SignInScreen(
        modifier = Modifier,
        uiState = uiState,
        onAction = viewModel.controller::dispatch,
    )
}