package com.example.currencyconverterapp.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverterapp.ui.screens.home.HomeScreen
import com.example.currencyconverterapp.ui.screens.signin.SignInRoute
import com.example.currencyconverterapp.ui.screens.signup.SignUpRoute
import com.example.currencyconverterapp.ui.screens.welcome.WelcomeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Welcome.route,
    ) {
        composable(NavRoutes.Welcome.route) { WelcomeScreen(navController = navController) }

        composable(NavRoutes.SignIn.route) {
            SignInRoute(
                navigateBack = { navController.popBackStack() },
                navigateToScreen = { navController.navigate(NavRoutes.Home.route) }
            )
        }

        composable(NavRoutes.SignUp.route) {
            SignUpRoute(
                navigateToScreen = {
                    navController.navigate(NavRoutes.SignIn.route) {
                        popUpTo(NavRoutes.SignUp.route) {
                            inclusive = true
                        }
                    }
                },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Home.route) { HomeScreen(navController = navController) }
    }
}