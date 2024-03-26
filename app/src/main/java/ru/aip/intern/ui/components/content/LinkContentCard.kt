package ru.aip.intern.ui.components.content

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import ru.aip.intern.domain.internships.data.LinkContentItem

@Composable
fun LinkContentCard(content: LinkContentItem) {

    val uriHandler = LocalUriHandler.current

    ListItem(
        leadingContent = {
            Icon(imageVector = Icons.Outlined.Link, contentDescription = null)
        },
        headlineContent = { Text(text = content.title) },
        modifier = Modifier.clickable {
            uriHandler.openUri(content.link)
        }
    )
}