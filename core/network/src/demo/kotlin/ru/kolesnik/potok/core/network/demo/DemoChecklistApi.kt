package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.retrofit.ChecklistApi
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoChecklistApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : ChecklistApi {
    
    override suspend fun getChecklist(taskId: String): List<ChecklistTaskDTO> {
        // Find the task in our demo data
        val allAreas = dataSource.getFullLifeAreas()
        for (area in allAreas) {
            area.flows?.forEach { flow ->
                flow.tasks?.find { it.id == taskId }?.let { task ->
                    return task.checkList ?: emptyList()
                }
            }
        }
        
        return emptyList()
    }
    
    override suspend fun createChecklist(taskId: String, request: ChecklistRq): ChecklistDTO {
        // Return a mock checklist
        return ChecklistDTO(
            checklist = request.checklist.mapIndexed { index, item ->
                ChecklistTaskDTO(
                    id = UUID.randomUUID(),
                    title = item.title,
                    done = item.done ?: false,
                    placement = index + 1,
                    responsibles = item.responsibles,
                    deadline = item.deadline
                )
            }
        )
    }
    
    override suspend fun moveChecklistTask(request: ChecklistTaskMoveRq) {
        // No-op in demo mode
    }
    
    override suspend fun updateChecklistTaskTitle(
        checklistTaskId: UUID,
        request: ChecklistTaskTitleRq
    ): ChecklistTaskDTO {
        // Return a mock updated checklist task
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = request.title,
            done = false,
            placement = 1
        )
    }
    
    override suspend fun deleteChecklistTask(checklistTaskId: UUID) {
        // No-op in demo mode
    }
    
    override suspend fun updateChecklistTaskDeadline(
        checklistTaskId: UUID,
        request: ChecklistTaskDeadlineRq
    ): ChecklistTaskDTO {
        // Return a mock updated checklist task
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = "Updated Task",
            done = false,
            placement = 1,
            deadline = request.deadline
        )
    }
    
    override suspend fun updateChecklistTaskResponsibles(
        checklistTaskId: UUID,
        request: ChecklistTaskResponsiblesRq
    ): ChecklistTaskDTO {
        // Return a mock updated checklist task
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = "Updated Task",
            done = false,
            placement = 1,
            responsibles = request.responsibles
        )
    }
    
    override suspend fun updateChecklistTaskStatus(
        checklistTaskId: UUID,
        done: Boolean
    ): ChecklistTaskDTO {
        // Return a mock updated checklist task
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = "Updated Task",
            done = done,
            placement = 1
        )
    }
    
    override suspend fun createChecklistTask(
        taskId: String,
        request: ChecklistTaskTitleRq
    ): ChecklistTaskDTO {
        // Return a mock checklist task
        return ChecklistTaskDTO(
            id = UUID.randomUUID(),
            title = request.title,
            done = false,
            placement = 1
        )
    }
}