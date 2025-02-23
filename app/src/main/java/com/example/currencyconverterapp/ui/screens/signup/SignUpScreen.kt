package com.example.currencyconverterapp.ui.screens.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.ui.components.CPOutlinedTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    uiState: SignUpState,
    onAction: (SignUpAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler { onAction(SignUpAction.CloseScreen) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onAction(SignUpAction.CloseScreen) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = Icons.AutoMirrored.Filled.ArrowBack.name
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
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
                    .padding(horizontal = 16.dp),
                onClick = { onAction(SignUpAction.Register) }
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = stringResource(R.string.register),
                    fontSize = 20.sp,
                )
            }
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() = SignUpScreen(
    modifier = Modifier,
    uiState = SignUpState(),
    onAction = {},
)