package ru.unilms.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import ru.unilms.R
import ru.unilms.app.UniAppScreen
import ru.unilms.data.DataStore
import ru.unilms.viewmodels.MenuViewModel

@Composable
fun MenuScreen(navigate: (UniAppScreen) -> Unit, dataStore: DataStore) {

    val viewModel = hiltViewModel<MenuViewModel>()

    val scope = rememberCoroutineScope()
    Column {
        enumValues<UniAppScreen>().forEach { screen ->
            if (screen.showInDrawer && screen.icon != null) {
                ListItem(
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                        .clickable {
                            navigate(screen)
                        },
                    leadingContent = {
                        Icon(
                            screen.icon,
                            null,
                        )
                    },
                    headlineContent = { Text(text = stringResource(id = screen.title)) },
                )
            }
        }


        ListItem(
            modifier = Modifier
                .padding(NavigationDrawerItemDefaults.ItemPadding)
                .clickable {
                    scope.launch {
                        dataStore.updateToken("")
                    }
                },
            leadingContent = {
                Icon(
                    Icons.Outlined.Logout,
                    null,
                )
            },
            headlineContent = { Text(text = stringResource(R.string.button_clear_token)) },
        )

        Divider()

        ListItem(
            modifier = Modifier
                .padding(NavigationDrawerItemDefaults.ItemPadding),
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.BugReport,
                    contentDescription = null
                )
            },
            headlineContent = { Text(text = viewModel.getUserName()) }
        )
    }
}