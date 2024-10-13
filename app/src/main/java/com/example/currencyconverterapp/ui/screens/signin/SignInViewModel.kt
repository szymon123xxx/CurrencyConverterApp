package com.example.currencyconverterapp.ui.screens.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschuster.control.Controller
import at.florianschuster.control.createController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(): ViewModel() {
    val controller = viewModelScope.createSignInController(
        initialSignInState = SignInState()
    )
}

data class SignInState(
    val email: String = "",
    val password: String = "",
    val isBiometricPromptShown: Boolean = false,
)

sealed interface SignInAction {
    data class UpdateEmailInput(val input: String): SignInAction
    data class UpdatePasswordInput(val input: String): SignInAction
}

sealed interface SignInMutation {
    data object Success: SignInMutation
    data class UpdateEmail(val email: String): SignInMutation
    data class UpdatePassword(val email: String): SignInMutation
}

//for now dummy
internal fun CoroutineScope.createSignInController(
    initialSignInState: SignInState
): Controller<SignInAction, SignInState> = createController(
    initialState = initialSignInState,
    mutator = { action ->
        when (action) {
            is SignInAction.UpdateEmailInput -> {
                flow {
                    emit(SignInMutation.UpdateEmail("ELO"))
                }
            }

            is SignInAction.UpdatePasswordInput -> {
                flow {
                    emit(SignInMutation.UpdateEmail("ELO"))
                }
            }
        }
    },
    reducer = { mutation: SignInMutation, previousState ->
        when(mutation) {
            is SignInMutation.UpdateEmail -> {
                previousState.copy(email = "", password = "", isBiometricPromptShown = false)
            }

            is SignInMutation.UpdatePassword -> {
                previousState.copy(email = "", password = "", isBiometricPromptShown = false)
            }

            is SignInMutation.Success -> {
                previousState.copy(email = "", password = "", isBiometricPromptShown = false)

            }
        }
    }
)