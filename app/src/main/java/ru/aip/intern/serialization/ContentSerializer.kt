package ru.aip.intern.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.aip.intern.domain.internships.data.AssignmentContentItem
import ru.aip.intern.domain.internships.data.BaseContentItem
import ru.aip.intern.domain.internships.data.FileContentItem
import ru.aip.intern.domain.internships.data.LinkContentItem
import ru.aip.intern.domain.internships.data.TextContentItem

object ContentSerializer :
    JsonContentPolymorphicSerializer<BaseContentItem>(BaseContentItem::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BaseContentItem> {
        return when (element.jsonObject["contentType"]?.jsonPrimitive?.content) {
            "text" -> TextContentItem.serializer()
            "file" -> FileContentItem.serializer()
            "link" -> LinkContentItem.serializer()
            "assignment" -> AssignmentContentItem.serializer()
            else -> throw SerializationException("No serializer was found")
        }
    }
}