package ru.aip.intern.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ru.aip.intern.navigation.Screen
import ru.aip.intern.ui.components.BaseScreen
import ru.aip.intern.viewmodels.AssignmentViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.UUID

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AssignmentScreen(
    title: MutableState<String>,
    assignmentId: UUID,
    navigate: (Screen, UUID) -> Unit
) {
    val viewModel = hiltViewModel<AssignmentViewModel, AssignmentViewModel.Factory>(
        creationCallback = { factory -> factory.create(assignmentId) }
    )
    val refreshing = viewModel.isRefreshing.observeAsState(false)
    val assignmentData = viewModel.assignmentData.observeAsState(viewModel.defaultContent)

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing.value,
        onRefresh = { viewModel.refresh() }
    )

    LaunchedEffect(assignmentData.value) {
        title.value = assignmentData.value.title
    }

    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        BaseScreen {
            if (assignmentData.value.description != null) {
                Text(text = assignmentData.value.description!!)
            }
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Scale,
                        contentDescription = null
                    )
                },
                headlineContent = { Text(text = "Дедлайн") },
                trailingContent = {
                    Text(
                        text = assignmentData.value.deadline.atZone(ZoneId.of("UTC"))
                            .withZoneSameInstant(
                                ZoneId.systemDefault()
                            ).format(
                                DateTimeFormatter.ofLocalizedDateTime(
                                    FormatStyle.SHORT
                                )
                            )
                    )
                }
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        navigate(Screen.File, assignmentData.value.fileId)
                    }
                ) {
                    Text(text = "Открыть файл")
                }
            }

        }
        PullRefreshIndicator(
            refreshing.value,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}