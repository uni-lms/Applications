package ru.unilms.domain.manage.model

import kotlinx.serialization.Serializable
import ru.unilms.domain.auth.util.enums.UserRole
import ru.unilms.domain.common.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class User(
    @Serializable(UUIDSerializer::class)
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val patronymic: String?,
    val roleName: UserRole,
)
