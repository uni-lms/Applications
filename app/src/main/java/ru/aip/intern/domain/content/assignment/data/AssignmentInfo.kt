package ru.aip.intern.domain.content.assignment.data

import kotlinx.serialization.Serializable
import ru.aip.intern.serialization.LocalDateTimeSerializer
import ru.aip.intern.serialization.UuidSerializer
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class AssignmentInfo(
    @Serializable(UuidSerializer::class)
    val id: UUID,
    val title: String,
    val description: String?,
    @Serializable(LocalDateTimeSerializer::class)
    val deadline: LocalDateTime,
    @Serializable(UuidSerializer::class)
    val fileId: UUID?
)