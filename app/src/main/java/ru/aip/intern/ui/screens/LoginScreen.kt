package ru.aip.intern.ui.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.aip.intern.navigation.Screen
import ru.aip.intern.permissions.PermissionStatus
import ru.aip.intern.ui.components.BaseScreen
import ru.aip.intern.ui.dialogs.RequestingNotificationPermissionDialog
import ru.aip.intern.viewmodels.LoginViewModel
import ru.aip.intern.viewmodels.PermissionManagerViewModel
import ru.aip.intern.viewmodels.StartScreenViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginScreen(
    title: MutableState<String>,
    navigateTo: (Screen) -> Unit
) {

    title.value = "Вход в аккаунт"

    val viewModel: LoginViewModel = hiltViewModel()
    val permissionsViewModel: PermissionManagerViewModel = hiltViewModel()
    val startScreenViewModel: StartScreenViewModel = hiltViewModel()

    fun submit() {
        if (viewModel.validate()) {
            viewModel.login { navigateTo(Screen.Internships) }
            startScreenViewModel.updateStartScreen(Screen.Internships)
        }
    }

    BaseScreen {

        val email = viewModel.email.observeAsState("")
        val password = viewModel.password.observeAsState("")
        val formEnabledState = viewModel.formEnabled.observeAsState(true)
        val isEmailWithError = viewModel.isEmailWithError.observeAsState(false)
        val emailErrorMessage = viewModel.emailErrorMessage.observeAsState("")
        val isPasswordWithError = viewModel.isPasswordWithError.observeAsState(false)
        val passwordErrorMessage = viewModel.passwordErrorMessage.observeAsState("")

        val refreshing = viewModel.isRefreshing.observeAsState(false)
        val pullRefreshState = rememberPullRefreshState(
            refreshing = refreshing.value,
            onRefresh = { }
        )

        val context = LocalContext.current

        val notificationPermissionStatus = remember {
            mutableStateOf(PermissionStatus.NOT_REQUESTED)
        }
        val notificationPermissionDialogStatus = remember {
            mutableStateOf(false)
        }
        val askedForNotificationPermission =
            viewModel.askedForNotificationPermission.observeAsState(false)


        LaunchedEffect(key1 = true) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionStatus.value = permissionsViewModel.checkPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                )

                notificationPermissionDialogStatus.value =
                    notificationPermissionStatus.value == PermissionStatus.DENIED && !askedForNotificationPermission.value

                viewModel.setAskedForNotificationPermission(true)
            }
        }


        Box {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { viewModel.setEmail(it) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    enabled = formEnabledState.value,
                    isError = isEmailWithError.value,
                    supportingText = {
                        if (isEmailWithError.value) {
                            Text(text = emailErrorMessage.value)
                        }
                    },
                    label = {
                        Text(
                            text = "Электропочта"
                        )
                    }
                )

                OutlinedTextField(
                    value = password.value,
                    onValueChange = { viewModel.setPassword(it) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            submit()
                        }
                    ),
                    isError = isPasswordWithError.value,
                    supportingText = {
                        if (isPasswordWithError.value) {
                            Text(text = passwordErrorMessage.value)
                        }
                    },
                    enabled = formEnabledState.value,
                    label = {
                        Text(
                            text = "Пароль"
                        )
                    }
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        submit()
                    },
                    enabled = formEnabledState.value
                ) {
                    Text(text = "Войти")
                }


            }

            PullRefreshIndicator(
                refreshing.value,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (notificationPermissionDialogStatus.value) {
                    RequestingNotificationPermissionDialog(
                        onDismissRequest = { },
                        onConfirmation = {
                            notificationPermissionDialogStatus.value = false
                            permissionsViewModel.requestPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                        },
                        onCancel = {
                            notificationPermissionDialogStatus.value = false
                        }
                    )
                }
            }
        }
    }
}