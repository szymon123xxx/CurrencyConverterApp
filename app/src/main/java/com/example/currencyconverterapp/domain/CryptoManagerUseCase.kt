package com.example.currencyconverterapp.domain

import com.example.currencyconverterapp.data.EncryptedData
import javax.crypto.Cipher

interface CryptoManagerUseCase {
    fun initEncryptionCipher(keyName: String): Cipher
    fun initDecryptionCipher(keyName: String, initializationVector: ByteArray): Cipher
    fun encrypt(plaintext: String, cipher: Cipher): EncryptedData
    fun decrypt(ciphertext: ByteArray, cipher: Cipher): String

    fun saveToPrefs(
        encryptedData: EncryptedData,
        filename: String,
        mode: Int,
        prefKey: String
    )

    fun getFromPrefs(
        filename: String,
        mode: Int,
        prefKey: String
    ): EncryptedData?
}