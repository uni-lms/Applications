package ru.unilms.domain.course.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import ru.unilms.R
import ru.unilms.data.AppBarState
import ru.unilms.domain.app.util.Screens
import ru.unilms.domain.course.model.CourseContent
import ru.unilms.domain.course.util.enums.CourseItemType
import ru.unilms.domain.course.view.component.item.FileItem
import ru.unilms.domain.course.view.component.item.QuizItem
import ru.unilms.domain.course.view.component.item.TaskItem
import ru.unilms.domain.course.view.component.item.TextItem
import ru.unilms.domain.course.viewmodel.CourseViewModel
import java.util.UUID

@Composable
fun CourseScreen(
    courseId: UUID,
    navigate: (Screens, UUID) -> Unit,
    onComposing: (AppBarState) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<CourseViewModel>()
    var courseContent: CourseContent? by remember { mutableStateOf(null) }

    fun updateCourseContent() = coroutineScope.launch {
        courseContent = viewModel.getCourseContent(courseId)
    }

    LaunchedEffect(true) {
        updateCourseContent()
    }

    LaunchedEffect(courseContent) {
        onComposing(
            AppBarState(
                title = if (courseContent != null) "${courseContent?.abbreviation} (${courseContent?.semester} семестр)" else null,
                actions = { }
            )
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Groups,
                    contentDescription = null
                )
            },
            headlineContent = { Text(text = "Комната ВКС для лекций (Google Meet)") }
        )
        ListItem(
            leadingContent = { Icon(Icons.Outlined.Book, null) },
            headlineContent = { Text(text = stringResource(id = R.string.screen_journal)) },
            modifier = Modifier.clickable {
                navigate(Screens.Journal, courseId)
            }
        )
        Divider()
        courseContent?.blocks?.forEach { block ->
            Text(
                text = stringResource(block.title.labelId),
                style = MaterialTheme.typography.titleMedium
            )
            block.items.forEach { item ->
                when (item.type) {
                    CourseItemType.File -> FileItem(
                        item = item,
                        onClick = { screen, id -> navigate(screen, id) })

                    CourseItemType.Quiz -> QuizItem(
                        item = item,
                        onClick = { screen, id -> navigate(screen, id) })

                    CourseItemType.Task -> TaskItem(
                        item = item,
                        onClick = { screen, id -> navigate(screen, id) })

                    CourseItemType.Text -> TextItem(
                        item = item,
                        onClick = { screen, id -> navigate(screen, id) }
                    )
                }
            }
        }
    }
}