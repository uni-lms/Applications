package ru.aip.intern.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ru.aip.intern.domain.auth.data.WhoamiResponse
import ru.aip.intern.navigation.Screen
import ru.aip.intern.navigation.ScreenPosition
import ru.aip.intern.ui.components.BaseScreen
import ru.aip.intern.viewmodels.MenuViewModel
import ru.aip.intern.viewmodels.StartScreenViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuScreen(title: MutableState<String>, navigateTo: (Screen) -> Unit) {
    // TODO real API call to get notifications count
    title.value = "Меню"

    val viewModel: MenuViewModel = hiltViewModel()
    val startScreenViewModel: StartScreenViewModel = hiltViewModel()
    val refreshing = viewModel.isRefreshing.observeAsState(false)
    val whoami = viewModel.whoamiData.observeAsState(WhoamiResponse(email = "", fullName = ""))
    val unreadNotificationsCount = viewModel.unreadNotificationsCount.observeAsState(0)

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing.value,
        onRefresh = { viewModel.refresh() }
    )

    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        BaseScreen {

            ListItem(
                headlineContent = { Text(text = whoami.value.fullName) },
                supportingContent = { Text(text = whoami.value.email) },
                leadingContent = {
                    Icon(Icons.Outlined.Person, null)
                }
            )

            enumValues<Screen>().filter { screen ->
                screen.icon != null && screen.position == ScreenPosition.Menu
            }.forEach { screen ->
                ListItem(
                    headlineContent = { Text(text = screen.title) },
                    leadingContent = {
                        if (screen.icon != null) {
                            Icon(screen.icon, null)
                        }
                    },
                    trailingContent = {
                        if (screen == Screen.Notifications && unreadNotificationsCount.value > 0) {
                            Badge {
                                Text(text = unreadNotificationsCount.value.toString())
                            }
                        }
                    }
                )
            }

            ListItem(
                headlineContent = { Text(text = "Выйти из аккаунта") },
                leadingContent = {
                    Icon(Icons.AutoMirrored.Outlined.Logout, null)
                },
                modifier = Modifier.clickable {
                    viewModel.logOut()
                    navigateTo(Screen.Login)
                    startScreenViewModel.updateStartScreen(Screen.Login)
                }
            )

        }
        PullRefreshIndicator(
            refreshing.value,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}