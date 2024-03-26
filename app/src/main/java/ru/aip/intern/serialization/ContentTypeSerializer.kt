package ru.aip.intern.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.aip.intern.domain.internships.data.ContentType

object ContentTypeSerializer : KSerializer<ContentType> {
    override val descriptor = PrimitiveSerialDescriptor("ContentType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ContentType) {
        encoder.encodeString(value.typeName)
    }

    override fun deserialize(decoder: Decoder): ContentType {
        val typeName = decoder.decodeString()
        return ContentType.entries.first { it.typeName == typeName }
    }
}