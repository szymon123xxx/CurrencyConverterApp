package com.example.currencyconverterapp.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun HomeRoute(navigateBack: () -> Unit, ) = HomeRouteContent(navigateBack = navigateBack,)

@Composable
fun HomeRouteContent(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val uiState by viewModel.controller.state.collectAsStateWithLifecycle()

    //it's repetitive, extract it as a extension
    LaunchedEffect(viewModel.controller) {
        viewModel.controller.effects.onEach { effect ->
            when (effect) {
                is HomeEffect.Logout -> navigateBack()
            }
        }.launchIn(this)
    }

    HomeScreen(
        uiState = uiState,
        onAction = viewModel.controller::dispatch,
    )
}