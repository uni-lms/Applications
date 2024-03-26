package ru.aip.intern.domain.internships.data

import kotlinx.serialization.Serializable

@Serializable
data class ContentSection(
    val name: String,
    val items: List<BaseContentItem>
)
