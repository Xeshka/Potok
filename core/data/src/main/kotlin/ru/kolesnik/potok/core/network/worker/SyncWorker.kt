package ru.kolesnik.potok.core.network.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.network.repository.FullProjectRepository
import ru.kolesnik.potok.core.network.repository.TaskRepository
import javax.inject.Inject

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var repository: FullProjectRepository

    override suspend fun doWork(): Result {
        return try {
            (applicationContext as SyncDependencyContainer).inject(this)

            repository.sync()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

class TaskWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var repository: TaskRepository

    override suspend fun doWork(): Result {
        return try {
            val taskId = inputData.getString(TASK_ID_KEY)
                ?: return Result.failure()

            (applicationContext as TaskDependencyContainer).inject(this)

            repository.getTaskById(taskId)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
    fun createTaskWorkRequest(taskId: String): OneTimeWorkRequest {
        val inputData = Data.Builder()
            .putString(TaskWorker.TASK_ID_KEY, taskId)
            .build()

        return OneTimeWorkRequestBuilder<TaskWorker>()
            .setInputData(inputData)
            .build()
    }

    companion object {
        const val TASK_ID_KEY = "task_id"
    }
}

interface SyncDependencyContainer {
    fun inject(worker: SyncWorker)
}

interface TaskDependencyContainer {
    fun inject(worker: TaskWorker)
}

class AppContainer @Inject constructor(
    private val repositoryFull: FullProjectRepository,
    private val taskRepository: TaskRepository
) : SyncDependencyContainer, TaskDependencyContainer {

    override fun inject(worker: SyncWorker) {
        worker.repository = repositoryFull
    }

    override fun inject(worker: TaskWorker) {
        worker.repository = taskRepository
    }
}


@Module
@InstallIn(SingletonComponent::class)
interface WorkerModule {
    @Binds
    fun bindSyncDependencyContainer(container: AppContainer): SyncDependencyContainer

    @Binds
    fun bindTaskDependencyContainer(container: AppContainer): TaskDependencyContainer
}