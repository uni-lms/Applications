package ru.aip.intern.util

import androidx.navigation.NavHostController
import ru.aip.intern.navigation.Screen
import java.util.UUID

fun goToScreen(
    navController: NavHostController,
    screen: Screen,
    id: UUID? = null,
    saveEntry: Boolean = true
) {
    val route: String = if (id == null) {
        screen.name
    } else {
        "${screen.name}/$id"
    }
    navController.navigate(route) {
        if (!saveEntry) {
            popUpTo(navController.currentDestination?.route ?: "") {
                inclusive = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}