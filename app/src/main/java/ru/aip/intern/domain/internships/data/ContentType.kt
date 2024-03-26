package ru.aip.intern.domain.internships.data

import kotlinx.serialization.Serializable
import ru.aip.intern.serialization.ContentTypeSerializer

@Serializable(ContentTypeSerializer::class)
enum class ContentType(val typeName: String) {
    Text("text"),
    Link("link"),
    File("file"),
    Assignment("assignment")
}