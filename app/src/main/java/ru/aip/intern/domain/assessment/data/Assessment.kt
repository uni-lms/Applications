package ru.aip.intern.domain.assessment.data

import kotlinx.serialization.Serializable
import ru.aip.intern.serialization.UuidSerializer
import java.util.UUID

@Serializable
data class Assessment(
    @Serializable(UuidSerializer::class)
    val id: UUID,
    val title: String,
    val description: String?,
    val weight: Double,
    val score: Int?
)