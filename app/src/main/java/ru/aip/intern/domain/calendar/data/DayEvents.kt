package ru.aip.intern.domain.calendar.data

import kotlinx.serialization.Serializable

@Serializable
data class DayEvents(
    val day: Int,
    val month: Int,
    val year: Int,
    val events: List<BaseDayEvent>
)
