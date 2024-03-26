package ru.aip.intern.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.aip.intern.domain.internships.data.Internship
import java.util.UUID

@Composable
fun InternshipCard(internship: Internship, goToInternship: (UUID) -> Unit) {

    ListItem(
        modifier = Modifier
            .clickable(onClick = { goToInternship(internship.id) })
            .clip(MaterialTheme.shapes.medium),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            headlineColor = MaterialTheme.colorScheme.onSurface,
            supportingColor = MaterialTheme.colorScheme.onSurface,
            overlineColor = MaterialTheme.colorScheme.onSurface
        ),
        shadowElevation = 8.dp,
        tonalElevation = 5.dp,
        headlineContent = {
            Text(
                text = internship.name,
            )
        }
    )

}