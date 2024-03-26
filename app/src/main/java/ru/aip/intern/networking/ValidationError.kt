package ru.aip.intern.networking

import kotlinx.serialization.Serializable

@Serializable
data class ValidationError(
    val identifier: String,
    val errorMessage: String,
    val errorCode: String,
    val severity: Int
)
