package ru.aip.intern.domain.calendar.data

import kotlinx.serialization.Serializable

@Serializable
data class DayEventsOverview(
    val dayOfMonth: Int,
    val hasDeadlines: Boolean,
)
