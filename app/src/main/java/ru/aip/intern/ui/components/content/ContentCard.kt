package ru.aip.intern.ui.components.content

import androidx.compose.runtime.Composable
import ru.aip.intern.domain.internships.data.AssignmentContentItem
import ru.aip.intern.domain.internships.data.BaseContentItem
import ru.aip.intern.domain.internships.data.FileContentItem
import ru.aip.intern.domain.internships.data.LinkContentItem
import ru.aip.intern.domain.internships.data.TextContentItem
import ru.aip.intern.navigation.Screen
import java.util.UUID

@Composable
fun ContentCard(content: BaseContentItem, navigate: (Screen, UUID) -> Unit) {
    if (content is TextContentItem) {
        TextContentCard(content = content)
    }

    if (content is LinkContentItem) {
        LinkContentCard(content = content)
    }

    if (content is FileContentItem) {
        FileContentCard(content = content) { id ->
            navigate(Screen.File, id)
        }
    }

    if (content is AssignmentContentItem) {
        AssignmentContentCard(content = content) { id ->
            navigate(Screen.Assignment, id)
        }
    }
}