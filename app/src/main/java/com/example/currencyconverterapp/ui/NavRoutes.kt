package com.example.currencyconverterapp.ui

sealed class NavRoutes(val route: String) {
    object Welcome : NavRoutes("welcome_screen")
    object SignIn : NavRoutes("sign_in_screen")
    object SignUp : NavRoutes("sign_up_screen)")
    object Home : NavRoutes("home_screen")
}