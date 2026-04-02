package com.potikot.doings.di

import android.app.Application
import androidx.room.Room
import com.potikot.doings.data.data_source.AppDatabase
import com.potikot.doings.data.data_source.repository.AccountRepositoryImpl
import com.potikot.doings.data.data_source.repository.BoardRepositoryImpl
import com.potikot.doings.data.data_source.repository.ColumnRepositoryImpl
import com.potikot.doings.data.data_source.repository.ProjectRepositoryImpl
import com.potikot.doings.data.data_source.repository.TagRepositoryImpl
import com.potikot.doings.data.data_source.repository.TaskRepositoryImpl
import com.potikot.doings.domain.remote.yougile.YougileApi
import com.potikot.doings.domain.repository.AccountRepository
import com.potikot.doings.domain.repository.AppDataRepository
import com.potikot.doings.domain.repository.BoardRepository
import com.potikot.doings.domain.repository.ColumnRepository
import com.potikot.doings.domain.repository.ProjectRepository
import com.potikot.doings.domain.repository.TagRepository
import com.potikot.doings.domain.repository.TaskRepository
import com.potikot.doings.domain.use_case.AddOrUpdateAccountUseCase
import com.potikot.doings.domain.use_case.AddOrUpdateBoardUseCase
import com.potikot.doings.domain.use_case.AddOrUpdateColumnUseCase
import com.potikot.doings.domain.use_case.AddOrUpdateProjectUseCase
import com.potikot.doings.domain.use_case.AddOrUpdateTagUseCase
import com.potikot.doings.domain.use_case.AddOrUpdateTaskUseCase
import com.potikot.doings.domain.use_case.DeleteAccountUseCase
import com.potikot.doings.domain.use_case.DeleteBoardUseCase
import com.potikot.doings.domain.use_case.DeleteColumnUseCase
import com.potikot.doings.domain.use_case.DeleteProjectUseCase
import com.potikot.doings.domain.use_case.DeleteTagUseCase
import com.potikot.doings.domain.use_case.DeleteTaskUseCase
import com.potikot.doings.domain.use_case.GetAccountUseCase
import com.potikot.doings.domain.use_case.GetAccountsUseCase
import com.potikot.doings.domain.use_case.GetBoardUseCase
import com.potikot.doings.domain.use_case.GetBoardsUseCase
import com.potikot.doings.domain.use_case.GetColumnsUseCase
import com.potikot.doings.domain.use_case.GetExternalAccountUseCase
import com.potikot.doings.domain.use_case.GetProjectUseCase
import com.potikot.doings.domain.use_case.GetProjectsUseCase
import com.potikot.doings.domain.use_case.GetTagsUseCase
import com.potikot.doings.domain.use_case.GetTasksCountUseCase
import com.potikot.doings.domain.use_case.GetTasksUseCase
import com.potikot.doings.domain.use_case.MainUseCases
import com.potikot.doings.domain.use_case.MoveTaskUseCase
import com.potikot.doings.domain.use_case.ProjectUseCases
import com.potikot.doings.domain.use_case.SelectBoardUseCase
import com.potikot.doings.domain.use_case.SelectColumnUseCase
import com.potikot.doings.domain.use_case.ToggleTaskCompletedUseCase
import com.potikot.doings.domain.use_case.UpdateProjectNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAppDataRepository(): AppDataRepository {
        return AppDataRepository()
    }

    @Provides
    @Singleton
    fun provideAccountRepository(db: AppDatabase, childRepository: ProjectRepository): AccountRepository {
        return AccountRepositoryImpl(db.accountDao, childRepository)
    }

    @Provides
    @Singleton
    fun provideProjectRepository(db: AppDatabase, childRepository: BoardRepository): ProjectRepository {
        return ProjectRepositoryImpl(db.projectDao, childRepository)
    }

    @Provides
    @Singleton
    fun provideBoardRepository(db: AppDatabase, childRepository: ColumnRepository): BoardRepository {
        return BoardRepositoryImpl(db.boardDao, childRepository)
    }

    @Provides
    @Singleton
    fun provideColumnRepository(db: AppDatabase, childRepository: TaskRepository): ColumnRepository {
        return ColumnRepositoryImpl(db.columnDao, childRepository)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: AppDatabase, childRepository: TagRepository): TaskRepository {
        return TaskRepositoryImpl(db.taskDao, childRepository)
    }

    @Provides
    @Singleton
    fun provideTagRepository(db: AppDatabase): TagRepository {
        return TagRepositoryImpl(db.tagDao)
    }

    @Provides
    @Singleton
    fun provideMainUseCases(
        accountRepository: AccountRepository,
        projectRepository: ProjectRepository
    ): MainUseCases {
        return MainUseCases(
            addOrUpdateAccount = AddOrUpdateAccountUseCase(accountRepository),
            getAccount = GetAccountUseCase(accountRepository),
            getAccounts = GetAccountsUseCase(accountRepository),
            getExternalAccount = GetExternalAccountUseCase(accountRepository),

            addOrUpdateProject = AddOrUpdateProjectUseCase(projectRepository),
            deleteProject = DeleteProjectUseCase(projectRepository),
            updateProjectName = UpdateProjectNameUseCase(projectRepository),
        )
    }

    @Provides
    @Singleton
    fun provideProjectUseCases(
        accountRepository: AccountRepository,
        projectRepository: ProjectRepository,
        boardRepository: BoardRepository,
        columnRepository: ColumnRepository,
        taskRepository: TaskRepository,
        tagRepository: TagRepository
    ): ProjectUseCases {
        return ProjectUseCases(
            addOrUpdateAccount = AddOrUpdateAccountUseCase(accountRepository),
            deleteAccount = DeleteAccountUseCase(accountRepository),
            getAccount = GetAccountUseCase(accountRepository),
            getAccounts = GetAccountsUseCase(accountRepository),

            addOrUpdateProject = AddOrUpdateProjectUseCase(projectRepository),
            deleteProject = DeleteProjectUseCase(projectRepository),
            getProject = GetProjectUseCase(projectRepository),
            getProjects = GetProjectsUseCase(projectRepository),

            addOrUpdateBoard = AddOrUpdateBoardUseCase(boardRepository),
            deleteBoard = DeleteBoardUseCase(boardRepository),
            getBoard = GetBoardUseCase(boardRepository),
            getBoards = GetBoardsUseCase(boardRepository),
            selectBoard = SelectBoardUseCase(projectRepository),

            addOrUpdateColumn = AddOrUpdateColumnUseCase(columnRepository),
            deleteColumn = DeleteColumnUseCase(columnRepository),
            getColumns = GetColumnsUseCase(columnRepository),
            selectColumn = SelectColumnUseCase(boardRepository),

            addOrUpdateTask = AddOrUpdateTaskUseCase(taskRepository),
            toggleTaskCompleted = ToggleTaskCompletedUseCase(taskRepository),
            deleteTask = DeleteTaskUseCase(taskRepository),
            getTasks = GetTasksUseCase(taskRepository),
            getTasksCount = GetTasksCountUseCase(taskRepository),
            moveTask = MoveTaskUseCase(taskRepository),

            addOrUpdateTag = AddOrUpdateTagUseCase(tagRepository),
            deleteTag = DeleteTagUseCase(tagRepository),
            getTags = GetTagsUseCase(tagRepository),
        )
    }

    // todo: mb switch from singleton to avoid concurrent requests
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    @Named("yougile")
    fun provideYougileHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    classDiscriminatorMode = ClassDiscriminatorMode.NONE
                })
            }
        }
    }

    @Provides
    @Singleton
    fun provideYougileApi(@Named("yougile") client: HttpClient): YougileApi {
        return YougileApi(client)
    }
}