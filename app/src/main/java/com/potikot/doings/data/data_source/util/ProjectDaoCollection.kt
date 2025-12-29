package com.potikot.doings.data.data_source.util

import com.potikot.doings.data.data_source.dao.AccountDao
import com.potikot.doings.data.data_source.dao.BoardDao
import com.potikot.doings.data.data_source.dao.ColumnDao
import com.potikot.doings.data.data_source.dao.ProjectDao
import com.potikot.doings.data.data_source.dao.TagDao
import com.potikot.doings.data.data_source.dao.TaskDao

data class ProjectDaoCollection(
    val account: AccountDao,
    val project: ProjectDao,
    val board: BoardDao,
    val column: ColumnDao,
    val task: TaskDao,
    val tag: TagDao
)