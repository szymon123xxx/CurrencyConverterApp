package com.example.currencyconverterapp.ui.screens.signin

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschuster.control.EffectController
import at.florianschuster.control.createEffectController
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.domain.usecases.DataStorePreferencesUseCase
import com.example.currencyconverterapp.ui.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    preferences: DataStorePreferencesUseCase
) : ViewModel() {
    val controller = viewModelScope.createSignInController(
        initialSignInState = SignInState(),
        preferences = preferences,
    )
}

data class SignInState(
    val authState: SignInAuthState = SignInAuthState.None,
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val isBiometricAlertAvailable: Boolean = false,
)

data class FieldState(
    val value: String = "",
    val error: FieldStateError = FieldStateError.None,
)

sealed interface FieldStateError {
    data object None : FieldStateError
    data class Error(val message: Int) : FieldStateError
}

sealed interface SignInAuthState {
    data object None : SignInAuthState
    data class Error(@StringRes val error: Int) : SignInAuthState
}

sealed interface SignInAction {
    data class UpdateEmailInput(val email: String) : SignInAction
    data class UpdatePasswordInput(val password: String) : SignInAction
    data object Authorize : SignInAction
    data object CloseScreen: SignInAction
}

private sealed interface SignInMutation {
    data class UpdateEmail(val email: String) : SignInMutation
    data class UpdatePassword(val password: String) : SignInMutation
    data class SetEmailValidationError(@StringRes val message: Int) : SignInMutation
    data class SetPasswordValidationError(@StringRes val message: Int) : SignInMutation
    data class SetValidationError(@StringRes val message: Int) : SignInMutation
    data object RefreshFields : SignInMutation
}

sealed interface SignInEffect {
    data object SuccessfulAuthorizationTriggered : SignInEffect
    data object BackNavigationTriggered : SignInEffect
}

/**
 * When making project modularized, switch to internal with all fun/classes
 */
private fun CoroutineScope.createSignInController(
    initialSignInState: SignInState,
    preferences: DataStorePreferencesUseCase,
): EffectController<SignInAction, SignInState, SignInEffect> = createEffectController(
    initialState = initialSignInState,
    mutator = { action ->
        when (action) {
            is SignInAction.UpdateEmailInput -> flowOf(SignInMutation.UpdateEmail(action.email))

            is SignInAction.UpdatePasswordInput -> flowOf(SignInMutation.UpdatePassword(action.password))

            is SignInAction.Authorize -> with(currentState) {
                when {
                    email.value.isBlank() || password.value.isBlank() || !email.value.isValidEmail() -> flow {
                        if (email.value.isBlank()) {
                            emit(SignInMutation.SetEmailValidationError(R.string.provide_email))
                        } else if (!email.value.isValidEmail()) {
                            emit(SignInMutation.SetEmailValidationError(R.string.provide_valid_email))
                        }
                        if (password.value.isBlank()) {
                            emit(SignInMutation.SetPasswordValidationError(R.string.provide_password))
                        }
                    }

                    else -> flow {
                        emit(SignInMutation.RefreshFields)

                        if (email.value != preferences.getEmail() || password.value != preferences.getPassword())
                            emit(SignInMutation.SetValidationError(R.string.invalid_login_inputs))
                        else
                            emitEffect(SignInEffect.SuccessfulAuthorizationTriggered)
                    }
                }
            }

            is SignInAction.CloseScreen -> flow {
                emitEffect(SignInEffect.BackNavigationTriggered)
            }
        }
    },
    reducer = { mutation: SignInMutation, previousState ->
        when (mutation) {
            is SignInMutation.UpdateEmail -> previousState.copy(
                email = previousState.email.copy(
                    value = mutation.email,
                    error = FieldStateError.None
                )
            )

            is SignInMutation.UpdatePassword -> previousState.copy(
                password = previousState.password.copy(
                    value = mutation.password,
                    error = FieldStateError.None
                )
            )

            is SignInMutation.SetEmailValidationError -> previousState.copy(
                email = previousState.email.copy(
                    error = FieldStateError.Error(mutation.message)
                )
            )

            is SignInMutation.SetPasswordValidationError -> previousState.copy(
                password = previousState.password.copy(
                    error = FieldStateError.Error(mutation.message)
                )
            )

            is SignInMutation.SetValidationError -> previousState.copy(
                authState = SignInAuthState.Error(mutation.message)
            )

            is SignInMutation.RefreshFields -> previousState.copy(
                authState = SignInAuthState.None,
                email = previousState.email.copy(
                    error = FieldStateError.None
                ),
                password = previousState.password.copy(
                    error = FieldStateError.None
                )
            )
        }
    }
)

