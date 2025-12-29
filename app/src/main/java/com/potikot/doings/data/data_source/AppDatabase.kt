package com.potikot.doings.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.potikot.doings.data.data_source.dao.AccountDao
import com.potikot.doings.data.data_source.dao.BoardDao
import com.potikot.doings.data.data_source.dao.ColumnDao
import com.potikot.doings.data.data_source.dao.ProjectDao
import com.potikot.doings.data.data_source.dao.TagDao
import com.potikot.doings.data.data_source.dao.TaskDao
import com.potikot.doings.data.data_source.entity.AccountEntity
import com.potikot.doings.data.data_source.entity.BoardEntity
import com.potikot.doings.data.data_source.entity.ColumnEntity
import com.potikot.doings.data.data_source.entity.ProjectEntity
import com.potikot.doings.data.data_source.entity.TagEntity
import com.potikot.doings.data.data_source.entity.TaskEntity

@Database(
    entities = [
        AccountEntity::class,
        ProjectEntity::class,
        BoardEntity::class,
        ColumnEntity::class,
        TaskEntity::class,
        TagEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract val accountDao: AccountDao
    abstract val projectDao: ProjectDao
    abstract val boardDao: BoardDao
    abstract val columnDao: ColumnDao
    abstract val taskDao: TaskDao
    abstract val tagDao: TagDao

    companion object {
        const val DATABASE_NAME = "doings_db2"
    }
}