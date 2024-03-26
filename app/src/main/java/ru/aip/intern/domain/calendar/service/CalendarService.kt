package ru.aip.intern.domain.calendar.service

import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import ru.aip.intern.auth.AuthManager
import ru.aip.intern.domain.calendar.data.DayEvents
import ru.aip.intern.domain.calendar.data.MonthEvents
import ru.aip.intern.networking.HttpClientFactory
import ru.aip.intern.networking.Response
import ru.aip.intern.networking.safeRequest
import javax.inject.Inject

class CalendarService @Inject constructor(private val authManager: AuthManager) {
    suspend fun getMonthEvents(year: Int, month: Int): Response<MonthEvents> {
        val httpClient = HttpClientFactory.httpClient
        val authHeaderValue = authManager.getAuthHeaderValue()
        return httpClient.safeRequest {
            method = HttpMethod.Get
            url("/calendar/${year}/${month}")
            accept(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, authHeaderValue)
            }
        }
    }

    suspend fun getDayEvents(year: Int, month: Int, day: Int): Response<DayEvents> {
        val httpClient = HttpClientFactory.httpClient
        val authHeaderValue = authManager.getAuthHeaderValue()
        return httpClient.safeRequest {
            method = HttpMethod.Get
            url("/calendar/${year}/${month}/${day}")
            accept(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, authHeaderValue)
            }
        }
    }
}