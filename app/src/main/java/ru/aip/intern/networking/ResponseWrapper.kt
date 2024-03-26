package ru.aip.intern.networking

import kotlinx.serialization.Serializable

@Serializable
data class ResponseWrapper<T>(
    val value: T?,
    val status: Int,
    val isSuccess: Boolean,
    val successMessage: String,
    val correlationId: String,
    val errors: List<String>,
    val validationErrors: List<ValidationError>
)
