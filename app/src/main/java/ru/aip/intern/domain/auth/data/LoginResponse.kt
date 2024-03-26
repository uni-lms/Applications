package ru.aip.intern.domain.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val accessToken: String)