package com.example.currencyconverterapp.ui.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.ui.NavRoutes
import com.example.currencyconverterapp.ui.components.CPOutlinedTextField
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val uiState by viewModel.controller.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.controller) {
        viewModel.controller.effects.onEach {
            if (it == SignUpEffect.Success) {
                navController.navigate(NavRoutes.SignIn.route) {
                    popUpTo(NavRoutes.SignUp.route) {
                        inclusive = true
                    }
                }
            }
        }.launchIn(this)
    }

    SignUpScreenContent(
        uiState = uiState,
        navigateBack = navigateBack,
        onAction = viewModel.controller::dispatch
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreenContent(
    uiState: SignUpState,
    onAction: (SignUpAction) -> Unit,
    navigateBack: () -> Unit,
) = Scaffold(
    topBar = {
        CenterAlignedTopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
        )
    },
) { innerPadding ->
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 16.dp),
            text = stringResource(R.string.register),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 28.sp
        )
        Spacer(Modifier.size(4.dp))

        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 16.dp),
            text = stringResource(R.string.password_personal_information),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.outlineVariant,
            fontSize = 16.sp
        )
        Spacer(Modifier.size(20.dp))

        CPOutlinedTextField(
            value = uiState.username.value,
            onValueChange = { onAction(SignUpAction.UpdateUsername(it)) },
            isError = uiState.username.error is FieldStateError.Error,
            errorText = when (uiState.username.error) {
                is FieldStateError.None -> null
                is FieldStateError.Error -> stringResource(uiState.username.error.message)
            },
            title = stringResource(R.string.username_field),
            placeholder = stringResource(R.string.provide_name)
        )
        Spacer(modifier = Modifier.size(4.dp))

        CPOutlinedTextField(
            value = uiState.email.value,
            onValueChange = { onAction(SignUpAction.UpdateEmailInput(it)) },
            isError = uiState.email.error is FieldStateError.Error,
            errorText = when (uiState.email.error) {
                is FieldStateError.None -> null
                is FieldStateError.Error -> stringResource(uiState.email.error.message)
            },
            title = stringResource(R.string.email_field),
            placeholder = stringResource(R.string.provide_email)
        )
        Spacer(modifier = Modifier.size(4.dp))

        CPOutlinedTextField(
            value = uiState.password.value,
            onValueChange = { onAction(SignUpAction.UpdatePasswordInput(it)) },
            isError = uiState.password.error is FieldStateError.Error,
            errorText = when (uiState.password.error) {
                is FieldStateError.None -> null
                is FieldStateError.Error -> stringResource(uiState.password.error.message)
            },
            title = stringResource(R.string.password_field),
            placeholder = stringResource(R.string.provide_password)
        )
        Spacer(modifier = Modifier.size(4.dp))

        CPOutlinedTextField(
            value = uiState.reEnteredPassword.value,
            onValueChange = { onAction(SignUpAction.UpdateReEnteredPasswordInput(it)) },
            isError = uiState.reEnteredPassword.error is FieldStateError.Error,
            errorText = when (uiState.reEnteredPassword.error) {
                is FieldStateError.None -> null
                is FieldStateError.Error -> stringResource(uiState.reEnteredPassword.error.message)
            },
            title = stringResource(R.string.confirm_password_field),
            placeholder = stringResource(R.string.provide_confirm_password)
        )
        Spacer(modifier = Modifier.size(8.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(horizontal = 16.dp),
            onClick = { onAction(SignUpAction.Register) }
        ) {
            Text(text = stringResource(R.string.register), fontSize = 20.sp)
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() = SignUpScreenContent(
    uiState = SignUpState(),
    onAction = {},
    navigateBack = {}
)