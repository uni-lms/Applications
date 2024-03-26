package ru.aip.intern.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

object HttpClientFactory {
    const val baseUrl = "demo.uni-edu.ru/api/v2"
    val httpClient: HttpClient by lazy {
        HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json()
            }

            install(DefaultRequest)

            defaultRequest {
                url(scheme = "https", host = baseUrl)
            }
        }
    }
}