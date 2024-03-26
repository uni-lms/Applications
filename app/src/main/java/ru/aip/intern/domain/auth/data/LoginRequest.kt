package ru.aip.intern.domain.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)
