package ru.kolesnik.potok.core.network.model

enum class FlowStatus {
    COMPLETE, IN_PROGRESS, NEW, COMPLETED, CUSTOM
}

typealias EmployeeId = String
typealias FlowId = String
typealias LifeAreaId = String
typealias ChecklistTaskId = String
typealias TagsId = Int
typealias IsuStyle = String
typealias IsuDate = String
typealias TaskExternalId = String
typealias TaskInternalId = Long