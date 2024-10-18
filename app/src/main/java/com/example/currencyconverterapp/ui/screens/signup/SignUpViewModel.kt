package com.example.currencyconverterapp.ui.screens.signup

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschuster.control.EffectController
import at.florianschuster.control.createEffectController
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.domain.BiometricPreferencesUseCase
import com.example.currencyconverterapp.domain.DispatcherProvider
import com.example.currencyconverterapp.utils.isValidEmail
import com.example.currencyconverterapp.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    preferences: BiometricPreferencesUseCase,
) : ViewModel() {
    val controller = viewModelScope.createSignUpController(
        initialSignUpState = SignUpState(),
        dispatcherProvider = dispatcherProvider,
        preferences = preferences,
    )
}

data class SignUpState(
    val authState: SignUpAuthState = SignUpAuthState.None,
    val username: FieldState = FieldState(),
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val reEnteredPassword: FieldState = FieldState(),
)

data class FieldState(
    val value: String = "",
    val error: FieldStateError = FieldStateError.None,
)

sealed interface FieldStateError {
    data object None : FieldStateError
    data class Error(val message: Int) : FieldStateError
}

sealed interface SignUpAuthState {
    data object None : SignUpAuthState
    data object Waiting : SignUpAuthState
    data class Error(val error: Int) : SignUpAuthState
}

sealed interface SignUpAction {
    data class UpdateUsername(val username: String) : SignUpAction
    data class UpdateEmailInput(val email: String) : SignUpAction
    data class UpdatePasswordInput(val password: String) : SignUpAction
    data class UpdateReEnteredPasswordInput(val reEnteredPassword: String) : SignUpAction
    data object Register : SignUpAction
}

sealed interface SignUpMutation {
    data class UpdateEmail(val email: String) : SignUpMutation
    data class UpdatePassword(val password: String) : SignUpMutation
    data class UpdateReEnteredPassword(val reEnteredPassword: String) : SignUpMutation
    data class UpdateUsername(val username: String) : SignUpMutation
    data class SetEmailValidationError(@StringRes val message: Int) : SignUpMutation
    data class SetPasswordValidationError(@StringRes val message: Int) : SignUpMutation
    data class SetReEnteredPasswordValidationError(@StringRes val message: Int) : SignUpMutation
    data class SetUsernameValidationError(@StringRes val message: Int) : SignUpMutation
    data class SetValidationError(@StringRes val message: Int) : SignUpMutation
    data object SetLoading : SignUpMutation
}

sealed interface SignUpEffect {
    data object Success : SignUpEffect
}

/**
 * When making project modularized, switch to internal with all fun/classes
 */
private fun CoroutineScope.createSignUpController(
    initialSignUpState: SignUpState,
    dispatcherProvider: DispatcherProvider,
    preferences: BiometricPreferencesUseCase,
): EffectController<SignUpAction, SignUpState, SignUpEffect> = createEffectController(
    initialState = initialSignUpState,
    mutator = { action ->
        when (action) {
            is SignUpAction.UpdateEmailInput -> flowOf(SignUpMutation.UpdateEmail(action.email))

            is SignUpAction.UpdatePasswordInput -> flowOf(SignUpMutation.UpdatePassword(action.password))

            is SignUpAction.UpdateReEnteredPasswordInput -> flowOf(
                SignUpMutation.UpdateReEnteredPassword(
                    action.reEnteredPassword
                )
            )

            is SignUpAction.UpdateUsername -> flowOf(SignUpMutation.UpdateUsername(action.username))

            is SignUpAction.Register -> when {
                currentState.email.value.isBlank()
                        || currentState.password.value.isBlank()
                        || currentState.reEnteredPassword.value.isBlank()
                        || currentState.reEnteredPassword.value != currentState.password.value
                        || currentState.username.value.isBlank()
                        || !currentState.email.value.isValidEmail()
                        || currentState.password.value.isValidPassword() != -1 -> flow {

                    if (currentState.email.value.isBlank()) {
                        emit(SignUpMutation.SetEmailValidationError(R.string.provide_email))
                    } else if (!currentState.email.value.isValidEmail()) {
                        emit(SignUpMutation.SetEmailValidationError(R.string.provide_valid_email))
                    }

                    if (currentState.username.value.isBlank()) {
                        emit(SignUpMutation.SetUsernameValidationError(R.string.provide_username))
                    }

                    if (currentState.password.value.isBlank()) {
                        emit(SignUpMutation.SetPasswordValidationError(R.string.provide_password))
                    } else if (currentState.password.value.isValidPassword() != -1) {
                        emit(SignUpMutation.SetPasswordValidationError(currentState.password.value.isValidPassword()))
                    }

                    if (currentState.password.value.isNotEmpty() && currentState.reEnteredPassword.value != currentState.password.value) {

                        emit(SignUpMutation.SetReEnteredPasswordValidationError(R.string.password_differ))
                    } else if (currentState.reEnteredPassword.value.isBlank()) {
                        emit(SignUpMutation.SetReEnteredPasswordValidationError(R.string.provide_password))
                    }

                }

                else -> flow {
                    emit(SignUpMutation.SetLoading)

                    withContext(dispatcherProvider.IO) {
                        preferences.setEmail(currentState.email.value)
                        preferences.setPassword(currentState.password.value)
                        preferences.setUsername(currentState.username.value)

                        emitEffect(SignUpEffect.Success)
                    }
                }
            }
        }
    },
    reducer = { mutation: SignUpMutation, previousState ->
        when (mutation) {
            is SignUpMutation.UpdateEmail -> {
                previousState.copy(
                    email = previousState.email.copy(
                        value = mutation.email,
                        error = FieldStateError.None
                    )
                )
            }

            is SignUpMutation.UpdatePassword -> {
                previousState.copy(
                    password = previousState.password.copy(
                        value = mutation.password,
                        error = FieldStateError.None
                    )
                )
            }

            is SignUpMutation.UpdateReEnteredPassword -> {
                previousState.copy(
                    reEnteredPassword = previousState.reEnteredPassword.copy(
                        value = mutation.reEnteredPassword,
                        error = FieldStateError.None
                    )
                )
            }

            is SignUpMutation.UpdateUsername -> {
                previousState.copy(
                    username = previousState.username.copy(
                        value = mutation.username,
                        error = FieldStateError.None
                    )
                )
            }

            is SignUpMutation.SetEmailValidationError -> {
                previousState.copy(
                    email = previousState.email.copy(
                        error = FieldStateError.Error(mutation.message)
                    )
                )
            }

            is SignUpMutation.SetPasswordValidationError -> {
                previousState.copy(
                    password = previousState.password.copy(
                        error = FieldStateError.Error(mutation.message)
                    )
                )
            }

            is SignUpMutation.SetUsernameValidationError -> {
                previousState.copy(
                    username = previousState.username.copy(
                        error = FieldStateError.Error(mutation.message)
                    )
                )
            }

            is SignUpMutation.SetReEnteredPasswordValidationError -> {
                previousState.copy(
                    reEnteredPassword = previousState.reEnteredPassword.copy(
                        error = FieldStateError.Error(mutation.message)
                    )
                )
            }

            is SignUpMutation.SetValidationError -> {
                previousState.copy(
                    authState = SignUpAuthState.Error(mutation.message)
                )
            }

            is SignUpMutation.SetLoading -> {
                previousState.copy(
                    authState = SignUpAuthState.Waiting,
                    email = previousState.email.copy(error = FieldStateError.None),
                    password = previousState.password.copy(error = FieldStateError.None),
                    username = previousState.username.copy(error = FieldStateError.None),
                    reEnteredPassword = previousState.reEnteredPassword.copy(error = FieldStateError.None),
                )
            }
        }
    }
)