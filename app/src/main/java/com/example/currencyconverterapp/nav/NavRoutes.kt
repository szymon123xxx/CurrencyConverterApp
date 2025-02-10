package com.example.currencyconverterapp.nav

sealed class NavRoutes(val route: String) {
    object Welcome : NavRoutes(Screen.WELCOME.name)
    object SignIn : NavRoutes(Screen.SIGN_IN.name)
    object SignUp : NavRoutes(Screen.SIGN_UP.name)
    object Home : NavRoutes(Screen.HOME.name)
}

enum class Screen {
    WELCOME,
    SIGN_IN,
    SIGN_UP,
    HOME,
}