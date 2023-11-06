package ru.unilms.domain.quiz.view.screen

import kotlinx.serialization.Serializable
import ru.unilms.domain.common.serialization.UUIDSerializer
import java.util.UUID

@Serializable
data class QuestionChoice(
    @Serializable(UUIDSerializer::class)
    val id: UUID,
    val title: String
)
