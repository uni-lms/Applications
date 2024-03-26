package ru.aip.intern.ui.components.content

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.aip.intern.domain.internships.data.FileContentItem
import java.util.UUID

@Composable
fun FileContentCard(content: FileContentItem, navigate: (UUID) -> Unit) {
    ListItem(
        leadingContent = {
            Icon(imageVector = Icons.Outlined.AttachFile, contentDescription = null)
        },
        headlineContent = {
            Text(text = content.title)
        },
        modifier = Modifier.clickable {
            navigate(content.id)
        }
    )
}