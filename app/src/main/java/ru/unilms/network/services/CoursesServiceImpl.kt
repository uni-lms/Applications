package ru.unilms.network.services

import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import ru.unilms.domain.model.courses.Course
import ru.unilms.domain.model.error.ErrorResponse
import ru.unilms.utils.enums.CourseType

class CoursesServiceImpl(
    private val client: HttpClient,
    private val baseUrl: String,
    private val token: String
) : CoursesService {
    override suspend fun getEnrolled(type: CourseType): Response<List<Course>, ErrorResponse> {
        return client.safeRequest {
            method = HttpMethod.Get
            parameter("filter", type.value)
            url("$baseUrl/v2/courses/enrolled")
            accept(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

}