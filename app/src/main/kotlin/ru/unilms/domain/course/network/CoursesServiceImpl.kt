package ru.unilms.domain.course.network

import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import ru.unilms.domain.common.model.ErrorResponse
import ru.unilms.domain.common.network.HttpClientFactory
import ru.unilms.domain.common.network.Response
import ru.unilms.domain.common.network.safeRequest
import ru.unilms.domain.course.model.Course
import ru.unilms.domain.course.model.CourseContent
import ru.unilms.domain.course.model.CourseTutor
import ru.unilms.domain.course.model.TextContentInfo
import ru.unilms.domain.course.util.enums.CourseType
import ru.unilms.domain.file.model.FileContentInfo
import java.util.UUID

class CoursesServiceImpl(
    private val token: String,
) : CoursesService {
    override suspend fun getEnrolled(type: CourseType): Response<List<Course>, ErrorResponse> {
        val client = HttpClientFactory.httpClient
        return client.safeRequest {
            method = HttpMethod.Get
            parameter("filter", type.value)
            url("${HttpClientFactory.baseUrl}/v2/courses/enrolled")
            accept(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun getOwned(): Response<List<CourseTutor>, ErrorResponse> {
        val client = HttpClientFactory.httpClient
        return client.safeRequest {
            method = HttpMethod.Get
            url("${HttpClientFactory.baseUrl}/v1/courses/owned")
            accept(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun getCourseContents(courseId: UUID): Response<CourseContent, ErrorResponse> {
        val client = HttpClientFactory.httpClient
        return client.safeRequest {
            method = HttpMethod.Get
            url("${HttpClientFactory.baseUrl}/v1/courses/${courseId}/contents")
            accept(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun getTextContent(textId: UUID): Response<ByteArray, ErrorResponse> {
        val client = HttpClientFactory.httpClient
        return client.safeRequest {
            method = HttpMethod.Get
            url("${HttpClientFactory.baseUrl}/v1/materials/text/${textId}")
            accept(ContentType.Text.Any)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun getTextContentInfo(textId: UUID): Response<TextContentInfo, ErrorResponse> {
        val client = HttpClientFactory.httpClient
        return client.safeRequest {
            method = HttpMethod.Get
            url("${HttpClientFactory.baseUrl}/v1/materials/text/${textId}/info")
            accept(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun getFileContentInfo(fileId: UUID): Response<FileContentInfo, ErrorResponse> {
        val client = HttpClientFactory.httpClient
        return client.safeRequest {
            method = HttpMethod.Get
            url("${HttpClientFactory.baseUrl}/v1/materials/file/${fileId}/info")
            accept(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

}