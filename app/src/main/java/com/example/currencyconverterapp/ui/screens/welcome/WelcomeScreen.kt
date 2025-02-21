package com.example.currencyconverterapp.ui.screens.welcome

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.nav.NavRoutes

@Composable
fun WelcomeScreen(navController: NavController) {
    BackHandler {}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
            .padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedPreloader()

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = { navController.navigate(NavRoutes.SignIn.route) },
        ) {
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = stringResource(R.string.sign_in),
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = { navController.navigate(NavRoutes.SignUp.route) },
        ) {
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = stringResource(R.string.sign_up),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
private fun ColumnScope.AnimatedPreloader(modifier: Modifier = Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.entry_animation))

    val preloaderProgress by animateLottieCompositionAsState(
        composition = preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    LottieAnimation(
        modifier = modifier.weight(1f),
        composition = preloaderLottieComposition,
        progress = { preloaderProgress },
    )
}