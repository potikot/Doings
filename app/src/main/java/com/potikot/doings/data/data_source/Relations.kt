package com.potikot.doings.data.data_source

import androidx.room.Embedded
import androidx.room.Relation
import com.potikot.doings.data.data_source.entity.AccountEntity
import com.potikot.doings.data.data_source.entity.BoardEntity
import com.potikot.doings.data.data_source.entity.ColumnEntity
import com.potikot.doings.data.data_source.entity.ProjectEntity
import com.potikot.doings.data.data_source.entity.TagEntity
import com.potikot.doings.data.data_source.entity.TaskEntity

data class AccountWithProjects(
    @Embedded val account: AccountEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "account_id",
        entity = ProjectEntity::class
    )
    val projects: List<ProjectWithBoards>
)

data class ProjectWithBoards(
    @Embedded val project: ProjectEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "project_id",
        entity = BoardEntity::class
    )
    val boards: List<BoardWithColumns>
)

data class BoardWithColumns(
    @Embedded val board: BoardEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "board_id",
        entity = ColumnEntity::class
    )
    val columns: List<ColumnWithTasks>
)

data class ColumnWithTasks(
    @Embedded val column: ColumnEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "column_id",
        entity = TaskEntity::class
    )
    val tasks: List<TaskWithTags>
)

data class TaskWithTags(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id",
        entity = TagEntity::class
    )
    val tags: List<TagEntity>
)