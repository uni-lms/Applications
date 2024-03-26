package ru.aip.intern.domain.calendar.data

import kotlinx.serialization.Serializable
import ru.aip.intern.serialization.EventTypeSerializer

@Serializable(EventTypeSerializer::class)
enum class EventType {
    Deadline
}