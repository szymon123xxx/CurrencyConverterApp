package com.example.currencyconverterapp.ui.screens.signup

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschuster.control.EffectController
import at.florianschuster.control.createEffectController
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.domain.usecases.DataStorePreferencesUseCase
import com.example.currencyconverterapp.ui.utils.isValidEmail
import com.example.currencyconverterapp.ui.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

typealias SignUpController = EffectController<SignUpAction, SignUpState, SignUpEffect>

@HiltViewModel
class SignUpViewModel @Inject constructor(preferences: DataStorePreferencesUseCase) : ViewModel() {
    val controller = viewModelScope.createSignUpController(
        initialSignUpState = SignUpState(),
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
    data object CloseScreen : SignUpAction
}

private sealed interface SignUpMutation {
    data class UpdateEmail(val email: String) : SignUpMutation
    data class UpdatePassword(val password: String) : SignUpMutation
    data class UpdateReEnteredPassword(val reEnteredPassword: String) : SignUpMutation
    data class UpdateUsername(val username: String) : SignUpMutation
    data class SetEmailValidationError(@StringRes val message: Int) : SignUpMutation
    data class SetPasswordValidationError(@StringRes val message: Int) : SignUpMutation
    data class SetReEnteredPasswordValidationError(@StringRes val message: Int) : SignUpMutation
    data class SetUsernameValidationError(@StringRes val message: Int) : SignUpMutation
    data class SetValidationError(@StringRes val message: Int) : SignUpMutation
    data object RefreshFields : SignUpMutation
}

sealed interface SignUpEffect {
    data object SuccessfulRegistrationTriggered : SignUpEffect
    data object BackNavigationTriggered : SignUpEffect
}

private fun CoroutineScope.createSignUpController(
    initialSignUpState: SignUpState,
    preferences: DataStorePreferencesUseCase,
): SignUpController = createEffectController(
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

            is SignUpAction.CloseScreen -> flow { emitEffect(SignUpEffect.BackNavigationTriggered) }

            is SignUpAction.Register -> with(currentState) {
                when {
                    currentState.email.value.isBlank()
                            || currentState.password.value.isBlank()
                            || currentState.reEnteredPassword.value.isBlank()
                            || currentState.reEnteredPassword.value != currentState.password.value
                            || currentState.username.value.isBlank()
                            || !currentState.email.value.isValidEmail()
                            || currentState.password.value.isValidPassword() != -1 -> flow {

                        if (email.value.isBlank()) {
                            emit(SignUpMutation.SetEmailValidationError(R.string.provide_email))
                        } else if (!email.value.isValidEmail()) {
                            emit(SignUpMutation.SetEmailValidationError(R.string.provide_valid_email))
                        }

                        if (username.value.isBlank()) {
                            emit(SignUpMutation.SetUsernameValidationError(R.string.provide_username))
                        }

                        if (password.value.isBlank()) {
                            emit(SignUpMutation.SetPasswordValidationError(R.string.provide_password))
                        } else if (password.value.isValidPassword() != -1) {
                            emit(SignUpMutation.SetPasswordValidationError(password.value.isValidPassword()))
                        }

                        if (password.value.isNotEmpty() && reEnteredPassword.value != currentState.password.value) {

                            emit(SignUpMutation.SetReEnteredPasswordValidationError(R.string.password_differ))
                        } else if (reEnteredPassword.value.isBlank()) {
                            emit(SignUpMutation.SetReEnteredPasswordValidationError(R.string.provide_password))
                        }

                    }

                    else -> flow {
                        with(preferences) {
                            setEmail(email.value)
                            setPassword(password.value)
                            setUsername(username.value)
                        }
                        emitEffect(SignUpEffect.SuccessfulRegistrationTriggered)
                    }
                }
            }
        }
    },
    reducer = { mutation: SignUpMutation, previousState ->
        when (mutation) {
            is SignUpMutation.UpdateEmail -> previousState.copy(
                email = previousState.email.copy(
                    value = mutation.email,
                    error = FieldStateError.None
                )
            )


            is SignUpMutation.UpdatePassword -> previousState.copy(
                password = previousState.password.copy(
                    value = mutation.password,
                    error = FieldStateError.None
                )
            )

            is SignUpMutation.UpdateReEnteredPassword -> previousState.copy(
                reEnteredPassword = previousState.reEnteredPassword.copy(
                    value = mutation.reEnteredPassword,
                    error = FieldStateError.None
                )
            )

            is SignUpMutation.UpdateUsername -> previousState.copy(
                username = previousState.username.copy(
                    value = mutation.username,
                    error = FieldStateError.None
                )
            )

            is SignUpMutation.SetEmailValidationError -> previousState.copy(
                email = previousState.email.copy(
                    error = FieldStateError.Error(mutation.message)
                )
            )

            is SignUpMutation.SetPasswordValidationError -> previousState.copy(
                password = previousState.password.copy(
                    error = FieldStateError.Error(mutation.message)
                )
            )

            is SignUpMutation.SetUsernameValidationError -> previousState.copy(
                username = previousState.username.copy(
                    error = FieldStateError.Error(mutation.message)
                )
            )

            is SignUpMutation.SetReEnteredPasswordValidationError -> previousState.copy(
                reEnteredPassword = previousState.reEnteredPassword.copy(
                    error = FieldStateError.Error(mutation.message)
                )
            )

            is SignUpMutation.SetValidationError -> previousState.copy(
                authState = SignUpAuthState.Error(mutation.message)
            )

            is SignUpMutation.RefreshFields -> previousState.copy(
                authState = SignUpAuthState.Waiting,
                email = previousState.email.copy(error = FieldStateError.None),
                password = previousState.password.copy(error = FieldStateError.None),
                username = previousState.username.copy(error = FieldStateError.None),
                reEnteredPassword = previousState.reEnteredPassword.copy(error = FieldStateError.None),
            )
        }
    }
)