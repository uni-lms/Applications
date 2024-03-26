package ru.aip.intern.domain.internships.data

import kotlinx.serialization.Serializable
import ru.aip.intern.serialization.UuidSerializer
import java.util.UUID

@Serializable
data class Internship(
    @Serializable(UuidSerializer::class)
    val id: UUID,
    val name: String
)
