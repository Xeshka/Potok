package ru.kolesnik.potok.core.network.model.potok

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.IsuDate
import ru.kolesnik.potok.core.network.model.LifeAreaId
import ru.kolesnik.potok.core.network.model.TaskExternalId

@Serializable
data class NetworkTaskPayload(
    var title: String? = null,
    val source: String? = null,
    var onMainPage: Boolean? = null,
    var deadline: IsuDate? = null,
    var lifeArea: String? = null,
    var lifeAreaId: LifeAreaId? = null,
    val subtitle: String? = null,
    var userEdit: Boolean? = null,
    var assignees: List<EmployeeId>? = null,
    var important: Boolean? = null,
    val messageId: String? = null,
    val fullMessage: String? = null,
    var description: String? = null,
    val priority: Int? = null,
    var userChangeAssignee: Boolean? = null,
    val organization: String? = null,
    val shortMessage: String? = null,
    var externalId: TaskExternalId? = null,
    val relatedAssignment: String? = null,
    var meanSource: String? = null,
    val id: String? = null,
)