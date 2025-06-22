package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import java.util.UUID
import java.time.LocalDate
import kotlinx.serialization.Contextual

@Serializable
data class LifeAreaDTO(
    @Contextual val id: UUID,
    val title: String,
    val flows: List<LifeFlowDTO>? = null,
    val style: String? = null,
    val tagsId: Long? = null,
    val placement: Int? = null,
    val isDefault: Boolean,
    val sharedInfo: LifeAreaSharedInfo? = null,
    val isTheme: Boolean,
    val onlyPersonal: Boolean
)

@Serializable
data class LifeAreaRq(
    val title: String,
    val style: String? = null,
    val tagsId: Long? = null,
    val placement: Int? = null,
    val isTheme: Boolean,
    val onlyPersonal: Boolean
)

@Serializable
data class LifeAreaSharedInfo(
    val owner: String,
    val readOnly: Boolean,
    @Contextual val expiredDate: LocalDate? = null,
    val recipients: List<String>
)

@Serializable
data class LifeAreaMoveDTO(
    @Contextual val lifeAreaId: UUID,
    val placement: Int
)

@Serializable
data class LifeAreaPosition(
    @Contextual val lifeAreaId: UUID,
    val position: Int? = null,
    val default: Boolean
)

@Serializable
data class LifeAreaPositionRq(
    @Contextual val lifeAreaId: UUID,
    val position: Int
)