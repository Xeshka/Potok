package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable

@Serializable
data class Op(
   val insert: String
)