package ru.unilms.app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ru.unilms.UniAppViewModel
import ru.unilms.screens.SelectApiUriScreen
import ru.unilms.screens.SignUpScreen

@Composable
fun UniApp() {
    val viewModel = hiltViewModel<UniAppViewModel>()

    viewModel.apiUri.ifEmpty {
        return SelectApiUriScreen()
    }

    viewModel.token.ifEmpty {
        return SignUpScreen()
    }
}