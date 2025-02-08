package com.example.currencyconverterapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CPOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorText: String?,
    title: String,
    placeholder: String,
    isPasswordType: Boolean = false
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = title,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 20.sp
        )
        Spacer(Modifier.size(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(100),
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            placeholder = {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = placeholder,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    fontSize = 16.sp
                )
            },
            supportingText = {
                errorText?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }

            },
            visualTransformation = if (isPasswordType) PasswordVisualTransformation() else VisualTransformation.None
        )
    }
}

@Preview
@Composable
fun CPOutlinedTextFieldPreview() =
    Box(
        modifier = Modifier
            .background(Color.White)
            .wrapContentSize()
    ) {
        CPOutlinedTextField(
            value = "",
            onValueChange = {},
            isError = false,
            errorText = "",
            placeholder = "Enter your email",
            title = "Email"
        )
    }