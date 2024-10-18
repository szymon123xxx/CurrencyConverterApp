package com.example.currencyconverterapp.utils

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.currencyconverterapp.data.BiometricPreferencesUseCaseImpl.PreferencesKey.ENCRYPTED_FILE_NAME
import com.example.currencyconverterapp.data.BiometricPreferencesUseCaseImpl.PreferencesKey.PREF_BIOMETRIC
import com.example.currencyconverterapp.data.BiometricPreferencesUseCaseImpl.PreferencesKey.SECRET_KEY
import com.example.currencyconverterapp.domain.CryptoManagerUseCase
import java.util.UUID

//Not completed
object BiometricHelper {

    fun isBiometricAvailable(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> {
                Log.e("TAG", "Biometric authentication not available")
                false
            }
        }
    }

    private fun getBiometricPrompt(
        context: FragmentActivity,
        onAuthSucceed: (BiometricPrompt.AuthenticationResult) -> Unit
    ): BiometricPrompt {
        val biometricPrompt =
            BiometricPrompt(
                context,
                ContextCompat.getMainExecutor(context),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        Log.e("TAG", "Authentication Succeeded: ${result.cryptoObject}")
                        onAuthSucceed(result)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        Log.e("TAG", "onAuthenticationError")
                        // TODO: Implement error handling
                    }

                    override fun onAuthenticationFailed() {
                        Log.e("TAG", "onAuthenticationFailed")
                        // TODO: Implement failure handling
                    }
                }
            )
        return biometricPrompt
    }

    private fun getPromptInfo(context: FragmentActivity): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Not defined")
            .setSubtitle("Not defined")
            .setDescription("Not defined")
            .setConfirmationRequired(false)
            .setNegativeButtonText("GO")
            .build()
    }

    fun registerUserBiometrics(
        context: FragmentActivity,
        onSuccess: (authResult: BiometricPrompt.AuthenticationResult) -> Unit = {},
        cryptoManagerUseCase: CryptoManagerUseCase
    ) {
        val biometricPrompt = getBiometricPrompt(context) { authResult ->
            authResult.cryptoObject?.cipher?.let { cipher ->
                val token = UUID.randomUUID().toString()
                val encryptedToken = cryptoManagerUseCase.encrypt(token, cipher)
                cryptoManagerUseCase.saveToPrefs(
                    encryptedToken,
                    ENCRYPTED_FILE_NAME,
                    Context.MODE_PRIVATE,
                    PREF_BIOMETRIC
                )
                onSuccess(authResult)
            }
        }
        biometricPrompt.authenticate(
            getPromptInfo(context),
            BiometricPrompt.CryptoObject(cryptoManagerUseCase.initEncryptionCipher(SECRET_KEY))
        )
    }

    fun authenticateUser(
        context: FragmentActivity,
        onSuccess: (plainText: String) -> Unit,
        cryptoManagerUseCase: CryptoManagerUseCase,
    ) {
        val encryptedData = cryptoManagerUseCase.getFromPrefs(
            ENCRYPTED_FILE_NAME,
            Context.MODE_PRIVATE,
            PREF_BIOMETRIC
        )

        encryptedData?.let { data ->
            val cipher = cryptoManagerUseCase.initDecryptionCipher(SECRET_KEY, data.initializationVector)
            val biometricPrompt = getBiometricPrompt(context) { authResult ->
                authResult.cryptoObject?.cipher?.let { cipher ->
                    onSuccess(cryptoManagerUseCase.decrypt(data.ciphertext, cipher))
                }
            }
            biometricPrompt.authenticate(getPromptInfo(context), BiometricPrompt.CryptoObject(cipher))
        }
    }
}