package ru.aip.intern.ui.fragments

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    canGoBack: Boolean,
    goUp: () -> Unit,
    modifier: Modifier = Modifier,
    title: MutableState<String>,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(title.value, style = MaterialTheme.typography.titleLarge)
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canGoBack) {
                IconButton(onClick = { goUp() }) {
                    Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                }
            }
        },
        actions = actions
    )
}