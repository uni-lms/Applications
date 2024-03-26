package ru.aip.intern.domain.calendar.data

import kotlinx.serialization.Serializable

@Serializable
data class MonthEvents(
    val year: Int,
    val month: Int,
    val days: List<DayEventsOverview>
)
