package ru.aip.intern.domain.content.file.data

import kotlinx.serialization.Serializable
import ru.aip.intern.serialization.UuidSerializer
import java.util.UUID

@Serializable
data class FileInfo(
    @Serializable(UuidSerializer::class)
    val id: UUID,
    val title: String,
    val fileSize: String,
    val fileName: String,
    val extension: String,
    val contentType: String
)