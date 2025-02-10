package com.example.currencyconverterapp.ui.screens.signin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun SignInScreen(
    modifier: Modifier,
    uiState: SignInState,
    onAction: (SignInAction) -> Unit,
) {
    BackHandler { onAction(SignInAction.CloseScreen) }

    //scaffold could be extracted outisae and there would be only on aligned top app bar, thing about it later
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onAction(SignInAction.CloseScreen) }) {
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
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CPOutlinedTextField(
                value = uiState.email.value,
                onValueChange = { onAction(SignInAction.UpdateEmailInput(it)) },
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
                onValueChange = { onAction(SignInAction.UpdatePasswordInput(it)) },
                isError = uiState.password.error is FieldStateError.Error,
                errorText = when (uiState.password.error) {
                    is FieldStateError.None -> null
                    is FieldStateError.Error -> stringResource(uiState.password.error.message)
                },
                title = stringResource(R.string.password_field),
                placeholder = stringResource(R.string.provide_password),
                isPasswordType = true,
            )
            Spacer(modifier = Modifier.size(8.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(horizontal = 16.dp),
                onClick = { onAction(SignInAction.Authorize) }
            ) {
                Text(text = stringResource(R.string.button_login), fontSize = 20.sp)
            }
        }
    }
}

@Preview
@Composable
fun SignInScreenPreview() = SignInScreen(
    modifier = Modifier,
    uiState = SignInState(),
    onAction = {},
)