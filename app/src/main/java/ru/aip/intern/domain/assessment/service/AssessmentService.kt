package ru.aip.intern.domain.assessment.service

import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import ru.aip.intern.auth.AuthManager
import ru.aip.intern.domain.assessment.data.InternAssessment
import ru.aip.intern.domain.assessment.data.InternsComparison
import ru.aip.intern.networking.HttpClientFactory
import ru.aip.intern.networking.Response
import ru.aip.intern.networking.safeRequest
import java.util.UUID
import javax.inject.Inject

class AssessmentService @Inject constructor(private val authManager: AuthManager) {

    suspend fun getInternsComparison(id: UUID): Response<InternsComparison> {
        val httpClient = HttpClientFactory.httpClient
        val authHeaderValue = authManager.getAuthHeaderValue()
        return httpClient.safeRequest {
            method = HttpMethod.Get
            url("/assessment/comparison/${id}")
            accept(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, authHeaderValue)
            }
        }
    }

    suspend fun getInternAssessment(id: UUID): Response<InternAssessment> {
        val httpClient = HttpClientFactory.httpClient
        val authHeaderValue = authManager.getAuthHeaderValue()
        return httpClient.safeRequest {
            method = HttpMethod.Get
            url("/assessments/intern/${id}")
            accept(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, authHeaderValue)
            }
        }
    }

}