package ru.aip.intern.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.aip.intern.navigation.Screen
import ru.aip.intern.snackbar.SnackbarMessageHandler
import ru.aip.intern.ui.components.BaseScreen
import ru.aip.intern.ui.components.ConfirmExit
import ru.aip.intern.ui.components.Greeting
import ru.aip.intern.ui.fragments.BottomBar
import ru.aip.intern.ui.fragments.SplashScreen
import ru.aip.intern.ui.fragments.TopBar
import ru.aip.intern.util.goToScreen
import ru.aip.intern.viewmodels.StartScreenViewModel
import java.util.UUID
import kotlin.random.Random

@Composable
fun AipApp(
    navController: NavHostController = rememberNavController(),
    snackbarMessageHandler: SnackbarMessageHandler
) {

    var showSplashScreen by remember { mutableStateOf(true) }

    // TODO real API call to get notifications count
    val hasUnreadNotifications by remember { mutableStateOf(Random.nextInt(0, 3) > 0) }

    val title = remember { mutableStateOf("AIP") }

    val snackbarHostState = remember { SnackbarHostState() }


    // FIXME эта ебаная хуйня почему-то не обновляет экран после логина, из-за чего не работает нижний док
    val viewModel: StartScreenViewModel = hiltViewModel()

    val startScreen = viewModel.startScreen.observeAsState(Screen.Login)
    val backStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(snackbarMessageHandler) {
        snackbarMessageHandler.message.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    ConfirmExit()

    if (showSplashScreen) {
        SplashScreen(onLoadingComplete = { screenName ->
            showSplashScreen = false

        })
    } else {
        Scaffold(
            topBar = {
                TopBar(
                    canGoBack = navController.previousBackStackEntry != null && startScreen.value.canGoBack,
                    goUp = { navController.navigateUp() },
                    title = title
                )
            },
            bottomBar = {
                if (startScreen.value.showBottomBar) {
                    BottomBar(navController = navController, hasUnreadNotifications)
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startScreen.value.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(Screen.Login.name) {
                    LoginScreen(title) { screen ->
                        goToScreen(
                            navController,
                            screen
                        )
                    }
                }
                composable(Screen.Internships.name) {
                    InternshipsScreen(title) { screen, id ->
                        goToScreen(
                            navController,
                            screen,
                            id
                        )
                    }
                }
                composable("${Screen.Internship.name}/{id}") {
                    val internshipId = backStackEntry?.arguments?.getString("id")
                    if (internshipId != null) {
                        InternshipScreen(
                            title,
                            UUID.fromString(internshipId),
                        ) { screen, id ->
                            goToScreen(
                                navController,
                                screen,
                                id
                            )
                        }
                    }
                }
                composable(Screen.Menu.name) {
                    MenuScreen(title) { screen -> goToScreen(navController, screen) }
                }

                composable(Screen.Calendar.name) {
                    CalendarScreen(title) { screen, id -> goToScreen(navController, screen, id) }
                }

                composable(Screen.Notifications.name) {
                    BaseScreen {
                        Greeting(name = "notifications")
                    }
                }

                composable("${Screen.File.name}/{id}") {
                    val fileId = backStackEntry?.arguments?.getString("id")
                    if (fileId != null) {
                        FileScreen(title, UUID.fromString(fileId))
                    }
                }

                composable("${Screen.Assignment.name}/{id}") {
                    val assignmentId = backStackEntry?.arguments?.getString("id")
                    if (assignmentId != null) {
                        AssignmentScreen(title, UUID.fromString(assignmentId)) { screen, id ->
                            goToScreen(navController, screen, id)
                        }
                    }
                }
            }
        }
    }
}
