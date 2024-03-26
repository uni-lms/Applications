package ru.aip.intern.ui.components.content

import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ru.aip.intern.domain.internships.data.TextContentItem

@Composable
fun TextContentCard(content: TextContentItem) {
    ListItem(
        headlineContent = {
            Text(
                text = content.text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}