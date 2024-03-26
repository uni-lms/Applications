package ru.aip.intern.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.aip.intern.domain.calendar.data.EventType

object EventTypeSerializer : KSerializer<EventType> {
    override val descriptor = PrimitiveSerialDescriptor("EventType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: EventType) {
        encoder.encodeString(value.name.lowercase())
    }

    override fun deserialize(decoder: Decoder): EventType {
        val typeName = decoder.decodeString()
        return EventType.entries.first { it.name.lowercase() == typeName }
    }
}