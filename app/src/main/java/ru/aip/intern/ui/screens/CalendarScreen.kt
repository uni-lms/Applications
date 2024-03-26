package ru.aip.intern.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.aip.intern.domain.calendar.data.DeadlineEvent
import ru.aip.intern.navigation.Screen
import ru.aip.intern.ui.components.BaseScreen
import ru.aip.intern.ui.components.calendar.Day
import ru.aip.intern.ui.components.calendar.DaysOfWeek
import ru.aip.intern.ui.components.calendar.events.DeadlineCard
import ru.aip.intern.viewmodels.CalendarViewModel
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarScreen(title: MutableState<String>, navigate: (Screen, UUID) -> Unit) {

    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val daysOfWeek = daysOfWeek()
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(6) }
    val endMonth = remember { currentMonth.plusMonths(6) }
    val calendarState = rememberCalendarState(
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
        startMonth = startMonth,
        endMonth = endMonth,
        outDateStyle = OutDateStyle.EndOfGrid
    )
    var isModelOpened by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf<CalendarDay?>(null) }

    val visibleMonth = rememberFirstCompletelyVisibleMonth(calendarState)

    val viewModel: CalendarViewModel = hiltViewModel<CalendarViewModel, CalendarViewModel.Factory>(
        creationCallback = { factory -> factory.create(visibleMonth.yearMonth) }
    )

    val refreshing = viewModel.isRefreshing.observeAsState(false)
    val dayRefreshing = viewModel.isDayRefreshing.observeAsState(false)
    val data = viewModel.data.observeAsState(viewModel.defaultContent)

    val dayEvents = viewModel.dayEvents.observeAsState(viewModel.defaultEvents)

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing.value,
        onRefresh = {
            title.value = buildTitle(visibleMonth.yearMonth)
            viewModel.refresh()
        }
    )

    val scope = rememberCoroutineScope()

    title.value = buildTitle(visibleMonth.yearMonth)


    LaunchedEffect(visibleMonth) {
        title.value = buildTitle(visibleMonth.yearMonth)
        isModelOpened = false
        selectedDay = null
        viewModel.yearMonth = visibleMonth.yearMonth
        viewModel.refresh()
    }

    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        BaseScreen {
            HorizontalCalendar(
                dayContent = {
                    Day(
                        day = it,
                        dayEventsOverview = data.value.days.firstOrNull { day -> day.dayOfMonth == it.date.dayOfMonth }) {
                        selectedDay = it
                        isModelOpened = true
                        viewModel.getDayEvents(it.date.dayOfMonth)
                    }
                },
                state = calendarState,
                monthHeader = {
                    DaysOfWeek(daysOfWeek = daysOfWeek)
                    Spacer(modifier = Modifier.height(25.dp))
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            calendarState.animateScrollToMonth(visibleMonth.yearMonth.previousMonth)
                        }
                    }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Outlined.ChevronLeft, contentDescription = null)
                        Text(text = "В прошлое")
                    }
                }
                Button(
                    onClick = {
                        scope.launch {
                            calendarState.animateScrollToMonth(visibleMonth.yearMonth.nextMonth)
                        }
                    }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "В будущее")
                        Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = null)

                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing.value,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }

    if (isModelOpened) {
        val onModalDismiss = {
            isModelOpened = false
            selectedDay = null
        }
        AlertDialog(
            onDismissRequest = onModalDismiss,
            confirmButton = {
                TextButton(onClick = onModalDismiss) {
                    Text(text = "Закрыть")
                }
            },
            title = {
                Text(
                    selectedDay?.date?.format(DateTimeFormatter.ofPattern("d MMMM, EEE")) ?: ""
                )
            },
            text = {
                if (dayRefreshing.value) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator()
                    }
                }
                if (dayEvents.value.events.isEmpty() && dayRefreshing.value.not()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Outlined.Inbox, null, modifier = Modifier.size(30.dp))
                        Text(
                            text = "Нет событий",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.padding(3.dp)) {
                        items(items = dayEvents.value.events, itemContent = {
                            if (it is DeadlineEvent) {
                                DeadlineCard(it) { screen, id ->
                                    isModelOpened = false
                                    navigate(screen, id)
                                }
                            }
                        })
                    }
                }
            }
        )
    }
}

@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.completelyVisibleMonths.firstOrNull() }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

private val CalendarLayoutInfo.completelyVisibleMonths: List<CalendarMonth>
    get() {
        val visibleItemsInfo = this.visibleMonthsInfo.toMutableList()
        return if (visibleItemsInfo.isEmpty()) {
            emptyList()
        } else {
            val lastItem = visibleItemsInfo.last()
            val viewportSize = this.viewportEndOffset + this.viewportStartOffset
            if (lastItem.offset + lastItem.size > viewportSize) {
                visibleItemsInfo.removeLast()
            }
            val firstItem = visibleItemsInfo.firstOrNull()
            if (firstItem != null && firstItem.offset < this.viewportStartOffset) {
                visibleItemsInfo.removeFirst()
            }
            visibleItemsInfo.map { it.month }
        }
    }

private fun buildTitle(yearMonth: YearMonth): String {
    return "${
        yearMonth.month.getDisplayName(
            TextStyle.FULL_STANDALONE,
            Locale.getDefault()
        )
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    } ${yearMonth.year}"
}