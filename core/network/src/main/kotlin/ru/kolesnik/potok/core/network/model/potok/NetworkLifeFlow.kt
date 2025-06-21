package ru.kolesnik.potok.core.network.model.potok

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.FlowId
import ru.kolesnik.potok.core.network.model.FlowStatus
import ru.kolesnik.potok.core.network.model.IsuStyle
import ru.kolesnik.potok.core.network.model.LifeAreaId

@Serializable
data class NetworkLifeFlow(
    val id: FlowId,
    val areaId: LifeAreaId,
    val title: String,
    val style: IsuStyle,
    val placement: Int?,
    val status: FlowStatus?,
    var tasks: List<NetworkTask>?,
)