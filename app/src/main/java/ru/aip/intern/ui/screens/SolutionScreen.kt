package ru.aip.intern.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.aip.intern.R
import ru.aip.intern.ui.components.BaseScreen
import ru.aip.intern.ui.components.comments.CommentTree
import ru.aip.intern.ui.components.content.FileContentCard
import ru.aip.intern.ui.state.SolutionState
import ru.aip.intern.util.format
import java.util.UUID

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen(
    state: SolutionState,
    onRefresh: () -> Unit,
    onFileClick: (UUID) -> Unit,
    onCommentTextUpdate: (String) -> Unit,
    onCommentCreate: (UUID?) -> Unit
) {


    val uriHandler = LocalUriHandler.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var replyCommentId by remember { mutableStateOf<UUID?>(null) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh
    )

    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        BaseScreen {
            ListItem(
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.AccessTime, contentDescription = null)

                },
                headlineContent = {
                    Text(text = stringResource(R.string.sent))
                },
                trailingContent = {
                    Text(
                        text = state.solutionInfo.createdAt.format()
                    )
                }
            )
            ListItem(
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
                },
                headlineContent = {
                    Text(text = stringResource(R.string.author))
                },
                trailingContent = {
                    Text(text = state.solutionInfo.author)
                }
            )
            if (state.solutionInfo.link != null) {
                ListItem(
                    leadingContent = {
                        Icon(imageVector = Icons.Outlined.Link, contentDescription = null)
                    },
                    headlineContent = {
                        Text(text = stringResource(R.string.solution_as_link))
                    },
                    trailingContent = {
                        Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = null)
                    },
                    modifier = Modifier.clickable {
                        uriHandler.openUri(state.solutionInfo.link)
                    }
                )
            }

            if (state.solutionInfo.files.isNotEmpty()) {
                Text(text = stringResource(R.string.solution_as_files))
            }

            state.solutionInfo.files.forEach {
                FileContentCard(content = it, navigate = onFileClick)
            }

            Text(text = stringResource(R.string.comments))

            CommentTree(comments = state.solutionInfo.comments) { commentId ->
                showBottomSheet = true
                replyCommentId = commentId
            }

            TextButton(onClick = {
                showBottomSheet = true
                replyCommentId = null
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Comment,
                    contentDescription = null,
                    Modifier.padding(10.dp)
                )
                Text(text = stringResource(R.string.comment_send))
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                        replyCommentId = null
                    },
                    sheetState = rememberModalBottomSheetState(),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.comment_send),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextField(
                                value = state.commentText,
                                onValueChange = onCommentTextUpdate,
                                Modifier.fillMaxWidth(0.9f)
                            )
                            IconButton(onClick = { onCommentCreate(replyCommentId) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.Send,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }

        }
        PullRefreshIndicator(
            state.isRefreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }

}