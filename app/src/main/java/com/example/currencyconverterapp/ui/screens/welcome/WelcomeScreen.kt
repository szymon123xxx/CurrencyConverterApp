package com.example.currencyconverterapp.ui.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.currencyconverterapp.ui.NavRoutes

@Composable
fun WelcomeScreen(navController: NavController) {

    //Animation here. lottie animation

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            onClick = { navController.navigate(NavRoutes.SignIn.route) },
        ) {
            Text(text = "Sign In", fontSize = 20.sp) //extract strings
        }
        Spacer(modifier = Modifier.size(10.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            onClick = { navController.navigate(NavRoutes.SignUp.route) },
        ) {
            Text(text = "Sign Up", fontSize = 20.sp)
        }
    }
}