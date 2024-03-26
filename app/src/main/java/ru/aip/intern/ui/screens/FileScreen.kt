package ru.aip.intern.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
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
import ru.aip.intern.ui.components.BaseScreen
import ru.aip.intern.viewmodels.FileViewModel
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FileScreen(
    title: MutableState<String>,
    fileId: UUID
) {

    val viewModel = hiltViewModel<FileViewModel, FileViewModel.Factory>(
        creationCallback = { factory -> factory.create(fileId) }
    )
    val refreshing = viewModel.isRefreshing.observeAsState(false)
    val fileData = viewModel.fileData.observeAsState(viewModel.defaultContent)

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing.value,
        onRefresh = { viewModel.refresh() }
    )

    LaunchedEffect(fileData.value) {
        title.value = fileData.value.title
    }

    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        BaseScreen {
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Scale,
                        contentDescription = null
                    )
                },
                headlineContent = { Text(text = "Размер файла") },
                trailingContent = { Text(text = fileData.value.fileSize) }
            )
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.AttachFile,
                        contentDescription = null
                    )
                },
                headlineContent = { Text(text = "Тип файла") },
                trailingContent = {
                    Text(
                        text = "Документ ${
                            fileData.value.extension.uppercase(
                                Locale.ROOT
                            ).replace(".", "")
                        }"
                    )
                }
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        viewModel.downloadFile()
                    }
                ) {
                    Text(text = "Скачать файл")
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