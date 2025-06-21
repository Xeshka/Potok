package ru.kolesnik.potok.core.network.model.potok

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.LifeAreaId
import ru.kolesnik.potok.core.network.model.TagsId

@Serializable
data class NetworkLifeArea(
    val id: LifeAreaId,
    val title: String,
    val flows: List<NetworkLifeFlow>? = emptyList(), //one to many
    val style: String?,
    val tagsId: TagsId?,
    val placement: Int?,
    val isDefault: Boolean,
    val sharedInfo: NetworkLifeAreaSharedInfo? = null, // one to one
    val isTheme: Boolean,
    val onlyPersonal: Boolean,
)