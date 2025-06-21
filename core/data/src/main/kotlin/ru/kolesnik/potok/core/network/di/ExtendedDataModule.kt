package ru.kolesnik.potok.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.network.datasource.ChecklistDataSource
import ru.kolesnik.potok.core.network.datasource.CommentDataSource
import ru.kolesnik.potok.core.network.datasource.impl.RetrofitChecklistDataSource
import ru.kolesnik.potok.core.network.datasource.impl.RetrofitCommentDataSource
import ru.kolesnik.potok.core.network.repository.ChecklistRepository
import ru.kolesnik.potok.core.network.repository.CommentRepository
import ru.kolesnik.potok.core.network.repository.impl.DefaultChecklistRepository
import ru.kolesnik.potok.core.network.repository.impl.DefaultCommentRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class ExtendedDataModule {

    // DataSources
    @Binds
    internal abstract fun bindsChecklistDataSource(
        checklistDataSource: RetrofitChecklistDataSource,
    ): ChecklistDataSource

    @Binds
    internal abstract fun bindsCommentDataSource(
        commentDataSource: RetrofitCommentDataSource,
    ): CommentDataSource

    // Repositories
    @Binds
    internal abstract fun bindsChecklistRepository(
        checklistRepository: DefaultChecklistRepository,
    ): ChecklistRepository

    @Binds
    internal abstract fun bindsCommentRepository(
        commentRepository: DefaultCommentRepository,
    ): CommentRepository
}