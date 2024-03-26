package ru.aip.intern.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.aip.intern.domain.calendar.data.BaseDayEvent
import ru.aip.intern.domain.calendar.data.DeadlineEvent

object EventsSerializer : JsonContentPolymorphicSerializer<BaseDayEvent>(BaseDayEvent::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BaseDayEvent> {
        return when (element.jsonObject["type"]?.jsonPrimitive?.content) {
            "deadline" -> DeadlineEvent.serializer()
            else -> throw SerializationException("No serializer was found")
        }
    }
}