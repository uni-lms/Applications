package ru.aip.intern.domain.internships.data

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val title: String,
    val sections: List<ContentSection>
)
