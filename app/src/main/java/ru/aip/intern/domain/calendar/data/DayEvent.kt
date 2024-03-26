package ru.aip.intern.domain.calendar.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import ru.aip.intern.serialization.EventTypeSerializer
import ru.aip.intern.serialization.EventsSerializer
import ru.aip.intern.serialization.UuidSerializer
import java.util.UUID

@Serializable(EventsSerializer::class)
sealed class BaseDayEvent {

    @OptIn(ExperimentalSerializationApi::class)
    @JsonNames("type")
    @Serializable(EventTypeSerializer::class)
    abstract val eventType: EventType
}

@Serializable
data class DeadlineEvent(
    @Serializable(UuidSerializer::class) val id: UUID,

    override val eventType: EventType = EventType.Deadline,

    val title: String
) : BaseDayEvent()