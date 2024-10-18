package com.example.currencyconverterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverterapp.ui.NavRoutes
import com.example.currencyconverterapp.ui.screens.home.HomeScreen
import com.example.currencyconverterapp.ui.screens.signin.SignInScreen
import com.example.currencyconverterapp.ui.screens.signup.SignUpScreen
import com.example.currencyconverterapp.ui.screens.welcome.WelcomeScreen
import com.example.currencyconverterapp.ui.theme.CurrencyConverterAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConverterAppTheme {
                App()
            }
        }
    }
}

@Composable
fun App() = Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Welcome.route,
    ) {
        composable(NavRoutes.Welcome.route) { WelcomeScreen(navController = navController) }
        composable(NavRoutes.SignIn.route) {
            SignInScreen(
                navController = navController,
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.SignUp.route) {
            SignUpScreen(
                navController = navController,
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.Home.route) { HomeScreen(navController = navController) }
    }
}