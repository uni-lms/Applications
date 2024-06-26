package ru.aip.intern.networking

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import ru.aip.intern.R
import ru.aip.intern.util.UiText

suspend inline fun <reified T> HttpClient.safeRequest(block: HttpRequestBuilder.() -> Unit): Response<T> {
    return try {
        val response = request {
            block()
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }

        when (response.status) {
            HttpStatusCode.OK -> Response(
                isSuccess = true,
                value = response.parseBody<T>()!!.value,
                errorMessage = null
            )

            HttpStatusCode.Unauthorized -> Response(
                isSuccess = false,
                errorMessage = if (response.headers["www-authenticate"] != null) UiText.DynamicText(
                    response.headers["www-authenticate"]!!
                ) else
                    UiText.StringResource(R.string.unauthorized),
                value = null
            )

            HttpStatusCode.Forbidden -> Response(
                isSuccess = false,
                errorMessage = UiText.StringResource(R.string.forbidden),
                value = null
            )

            HttpStatusCode.NotFound -> Response(
                isSuccess = false,
                errorMessage = UiText.StringResource(
                    R.string.not_found,
                    response.parseBody<T>()!!.errors[0]
                ),
                value = null
            )

            else -> {
                Response(
                    isSuccess = false,
                    value = null,
                    errorMessage = UiText.StringResource(R.string.unknown_error)
                )
            }
        }

    } catch (e: ClientRequestException) {
        Response(
            isSuccess = false,
            value = null,
            errorMessage = UiText.StringResource(R.string.client_error)
        )
    } catch (e: ServerResponseException) {
        Response(
            isSuccess = false,
            value = null,
            errorMessage = UiText.StringResource(R.string.server_error)
        )
    } catch (e: IOException) {
        Response(
            isSuccess = false,
            value = null,
            errorMessage = UiText.StringResource(R.string.network_error)
        )
    } catch (e: SerializationException) {
        Response(
            isSuccess = false,
            value = null,
            errorMessage = UiText.StringResource(R.string.parsing_error)
        )
    }
}

suspend inline fun HttpClient.safeDownload(
    url: String,
    crossinline progressListener: (Float) -> Unit,
    block: HttpRequestBuilder.() -> Unit = {}
): Response<ByteArray> {
    return try {

        val response = this.get(url) {
            onDownload { bytesSentTotal, contentLength ->
                val progress = when {
                    contentLength <= 0L -> 0f
                    bytesSentTotal == 0L -> 0f
                    else -> (bytesSentTotal.toFloat() / contentLength).coerceIn(0f, 1f)
                }

                progressListener(progress)
            }
            block()
        }

        when (response.status) {
            HttpStatusCode.OK -> Response(
                isSuccess = true,
                value = response.body<ByteArray>(),
                errorMessage = null
            )

            HttpStatusCode.Unauthorized -> Response(
                isSuccess = false,
                errorMessage = if (response.headers["www-authenticate"] != null) UiText.DynamicText(
                    response.headers["www-authenticate"]!!
                ) else
                    UiText.StringResource(R.string.unauthorized),
                value = null
            )

            HttpStatusCode.Forbidden -> Response(
                isSuccess = false,
                errorMessage = UiText.StringResource(R.string.forbidden),
                value = null
            )

            HttpStatusCode.NotFound -> Response(
                isSuccess = false,
                errorMessage = UiText.StringResource(
                    R.string.not_found,
                ),
                value = null
            )

            else -> {
                Response(
                    isSuccess = false,
                    value = null,
                    errorMessage = UiText.StringResource(R.string.unknown_error)
                )
            }
        }

    } catch (e: ClientRequestException) {
        Response(
            isSuccess = false,
            value = null,
            errorMessage = UiText.StringResource(R.string.client_error)
        )
    } catch (e: ServerResponseException) {
        Response(
            isSuccess = false,
            value = null,
            errorMessage = UiText.StringResource(R.string.server_error)
        )
    } catch (e: IOException) {
        Response(
            isSuccess = false,
            value = null,
            errorMessage = UiText.StringResource(R.string.network_error)
        )
    } catch (e: SerializationException) {
        Response(
            isSuccess = false,
            value = null,
            errorMessage = UiText.StringResource(R.string.parsing_error)
        )
    }
}

suspend inline fun <reified T> HttpResponse.parseBody(): ResponseWrapper<T>? {
    val json = Json { ignoreUnknownKeys = true }
    return try {
        val stringBody = bodyAsText()
        json.decodeFromString<ResponseWrapper<T>>(stringBody)
    } catch (e: SerializationException) {
        Log.d("http", e.message.toString())
        Log.d("http", e.stackTrace.toString())
        throw e
    }
}