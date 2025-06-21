package ru.kolesnik.potok.core.network.model.potok

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.FlowId
import ru.kolesnik.potok.core.network.model.IsuDate
import ru.kolesnik.potok.core.network.model.LifeAreaId
import ru.kolesnik.potok.core.network.model.TaskExternalId

@Serializable
data class NetworkCreateTask(
    val flowId: FlowId,
    val lifeAreaId: LifeAreaId,
    val payload: NetworkCreateTaskPayload,
){
    @Serializable
    data class NetworkCreateTaskPayload(
        var title: String? = null,
        var deadline: IsuDate? = null,
        var lifeArea: String? = null,
        var lifeAreaId: LifeAreaId? = null,
        var assignees: List<EmployeeId>? = null,
        var important: Boolean? = null,
        var description: String? = null,
        var externalId: TaskExternalId? = null,
    )
}