package ru.aip.intern.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val title: String,
    val canGoBack: Boolean = false,
    val showBottomBar: Boolean = true,
    val icon: ImageVector? = null,
    val position: ScreenPosition? = null
) {
    Login(
        title = "Вход",
        canGoBack = false,
        showBottomBar = false,
    ),
    Internships(
        title = "Стажировки",
        canGoBack = false,
        icon = Icons.AutoMirrored.Outlined.LibraryBooks,
        position = ScreenPosition.BottomBar,
    ),
    Internship(
        title = "Стажировка",
        canGoBack = true,
        icon = null,
        position = null,
    ),
    Calendar(
        title = "Календарь",
        canGoBack = false,
        icon = Icons.Outlined.CalendarMonth,
        position = ScreenPosition.BottomBar
    ),
    Menu(
        title = "Меню",
        canGoBack = false,
        icon = Icons.Outlined.Menu,
        position = ScreenPosition.BottomBar
    ),
    Notifications(
        title = "Уведомления",
        canGoBack = true,
        icon = Icons.Outlined.Notifications,
        position = ScreenPosition.Menu
    ),
    File(
        title = "Файл",
        canGoBack = true,
        icon = null,
        position = null
    ),
    Assignment(
        title = "Задание",
        canGoBack = true,
        icon = null,
        position = null
    )
}