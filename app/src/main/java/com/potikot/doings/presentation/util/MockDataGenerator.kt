package com.potikot.doings.presentation.util

import com.potikot.doings.domain.model.Account
import com.potikot.doings.domain.model.AccountId
import com.potikot.doings.domain.model.Board
import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.Column
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.Project
import com.potikot.doings.domain.model.ProjectId
import com.potikot.doings.domain.model.Tag
import com.potikot.doings.domain.model.Task
import com.potikot.doings.domain.model.TaskId
import java.time.Instant
import kotlin.random.Random

fun generateMockTask(
    title: String,
    description: String,
    tags: List<Tag> = emptyList()
): Task = Task(
    id = TaskId(0),
    externalId = "",
    name = title,
    description = description,
    isCompleted = Random.nextBoolean(),
    tags = tags,
    position = 0,
    createdAt = Instant.now()
)

fun generateMockColumn(
    name: String,
    taskCount: Int
): Column {
    val tasks = (1..taskCount).map {
        generateMockTask(
            title = "Task $it".repeat(if (it % 2 == 0) 20 else 1),
            description = "Description for task $it"
        )
    }
    return Column(
        id = ColumnId(0),
        externalId = "",
        name = name,
        position = 0,
        tasks = tasks,
        createdAt = Instant.now()
    )
}

fun generateMockBoard(
    name: String,
    columnCount: Int,
    tasksPerColumn: Int
): Board {
    val columns = (1..columnCount).map {
        generateMockColumn(
            name = "Column $it",
            taskCount = tasksPerColumn
        )
    }
    return Board(
        id = BoardId(0),
        externalId = "",
        name = name,
        columns = columns,
        position = 0,
        createdAt = Instant.now()
    )
}

fun generateMockProject(
    name: String,
    boardCount: Int,
    columnsPerBoard: Int,
    tasksPerColumn: Int
): Project {
    val boards = (1..boardCount).map {
        generateMockBoard(
            name = "Board $it",
            columnCount = columnsPerBoard,
            tasksPerColumn = tasksPerColumn
        )
    }
    return Project(
        id = ProjectId(0),
        externalId = "",
        name = name,
        boards = boards,
        createdAt = Instant.now()
    )
}

fun generateMockAccount(
    name: String,
    projectCount: Int,
    boardsPerProject: Int,
    columnsPerBoard: Int,
    tasksPerColumn: Int
): Account {
    val projects = (1..projectCount).map {
        generateMockProject(
            name = "Project $it",
            boardCount = boardsPerProject,
            columnsPerBoard = columnsPerBoard,
            tasksPerColumn = tasksPerColumn
        )
    }
    return Account(
        id = AccountId(0),
        externalId = "",
        name = name,
        projects = projects,
        createdAt = Instant.now()
    )
}
