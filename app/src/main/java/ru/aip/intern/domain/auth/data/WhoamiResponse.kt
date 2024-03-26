package ru.aip.intern.domain.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class WhoamiResponse(
    val email: String,
    val fullName: String
)
