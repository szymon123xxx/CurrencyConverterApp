package com.example.currencyconverterapp.ui.utils

import android.text.TextUtils
import android.util.Patterns
import com.example.currencyconverterapp.R

fun String.isValidEmail() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Int {
    if (this.length < 8) return R.string.password_limit_characters
    if (this.filter { it.isDigit() }.firstOrNull() == null) return R.string.password_limit_digit
    if (this.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return R.string.password_limit_uppercase
    if (this.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return R.string.password_limit_lowercase
    if (this.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return R.string.password_limit_special_character

    return -1
}