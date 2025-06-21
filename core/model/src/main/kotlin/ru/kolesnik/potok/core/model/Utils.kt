package ru.kolesnik.potok.core.model

import java.time.OffsetDateTime
import java.util.UUID

enum class FlowStatus{
    COMPLETE, IN_PROGRESS, NEW, COMPLETED, CUSTOM
}

typealias EmployeeId = String
typealias FlowId = UUID
typealias LifeAreaId = UUID
typealias ChecklistTaskId = UUID
typealias TagsId = Int
typealias IsuStyle = String
typealias IsuDate = OffsetDateTime
typealias TaskExternalId = String
typealias TaskInternalId = Long