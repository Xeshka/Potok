package ru.kolesnik.potok.core.model

import java.util.UUID


data class LifeArea(
    val id: UUID,
    val title: String,
    val style: String?,
    val tagsId: TagsId?,
    val placement: Int?,
    val isDefault: Boolean,
    val isTheme: Boolean,
    val shared: LifeAreaSharedInfo?
)

data class LifeAreaSharedInfo(
    val areaId: LifeAreaId,
    val owner: EmployeeId,
    val recipients: List<EmployeeId>,
)