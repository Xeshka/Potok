package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable

@Serializable
data class ErrorRs(
    val message: String? = null,
    val errorCode: String? = null
)