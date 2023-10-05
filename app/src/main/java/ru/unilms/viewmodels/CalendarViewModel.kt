package ru.unilms.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.unilms.domain.model.calendar.DayEvent
import ru.unilms.domain.model.calendar.DayEventsInfo
import ru.unilms.domain.model.calendar.MonthEventsInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    private val oct2023data = MonthEventsInfo(
        listOf(
            DayEventsInfo(1, hasEvents = true, hasLessons = true, hasDeadlines = true),
            DayEventsInfo(4, hasEvents = false, hasLessons = true, hasDeadlines = false),
            DayEventsInfo(8, hasEvents = true, hasLessons = false, hasDeadlines = false),
            DayEventsInfo(10, hasEvents = false, hasLessons = false, hasDeadlines = true)
        )
    )

    fun requestDataForMonth(monthYear: YearMonth): MonthEventsInfo {
        return when (monthYear) {
            YearMonth.of(2023, 10) -> oct2023data
            else -> MonthEventsInfo(listOf())
        }
    }

    fun requestDataForDay(date: LocalDate): List<DayEvent> {
        return when (date) {
            LocalDate.parse("2023-10-01") -> listOf(
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
            )

            LocalDate.parse("2023-10-04") -> listOf(
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
            )

            LocalDate.parse("2023-10-08") -> listOf(
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
            )

            LocalDate.parse("2023-10-10") -> listOf(
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
                DayEvent("Дедлайн по Л/Р №1", LocalDateTime.parse("2023-09-01T10:15:30")),
            )

            else -> listOf()
        }
    }
}