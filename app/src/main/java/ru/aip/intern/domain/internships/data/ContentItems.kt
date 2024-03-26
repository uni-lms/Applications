package ru.aip.intern.domain.internships.data

import kotlinx.serialization.Serializable
import ru.aip.intern.serialization.ContentSerializer
import ru.aip.intern.serialization.ContentTypeSerializer
import ru.aip.intern.serialization.UuidSerializer
import java.util.UUID

@Serializable(ContentSerializer::class)
sealed class BaseContentItem {
    @Serializable(ContentTypeSerializer::class)
    abstract val contentType: ContentType
}

@Serializable
data class TextContentItem(
    @Serializable(UuidSerializer::class) val id: UUID,

    override val contentType: ContentType = ContentType.Text,

    val text: String
) : BaseContentItem()

@Serializable
data class FileContentItem(
    @Serializable(UuidSerializer::class) val id: UUID,

    override val contentType: ContentType = ContentType.File,

    val title: String
) : BaseContentItem()

@Serializable
data class LinkContentItem(
    @Serializable(UuidSerializer::class) val id: UUID,

    override val contentType: ContentType = ContentType.Link,

    val link: String,
    val title: String
) : BaseContentItem()

@Serializable
data class AssignmentContentItem(
    @Serializable(UuidSerializer::class) val id: UUID,

    override val contentType: ContentType = ContentType.Assignment,

    val title: String
) : BaseContentItem()
