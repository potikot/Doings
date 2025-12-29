package com.potikot.doings.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.potikot.doings.data.data_source.TaskWithTags
import com.potikot.doings.data.data_source.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // region Insert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity): Long

    // endregion

    // region Update

    @Update
    suspend fun update(task: TaskEntity): Int

    @Query("UPDATE tasks SET is_completed = :isCompleted WHERE id = :id")
    suspend fun updateIsCompleted(id: Long, isCompleted: Boolean): Int

    @Query("UPDATE tasks SET position = :position WHERE id = :id")
    suspend fun updatePosition(id: Long, position: Int): Int

    @Query("UPDATE tasks SET column_id = :targetColumnId WHERE id = :id")
    suspend fun updateColumn(id: Long, targetColumnId: Long): Int

    // endregion

    // region Get

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getById(id: Long): Flow<TaskWithTags?>

    @Transaction
    @Query("SELECT * FROM tasks WHERE column_id = :columnId")
    fun getAllFromColumn(columnId: Long): Flow<List<TaskWithTags>>

    // endregion

    // region Delete

    @Delete
    suspend fun delete(task: TaskEntity): Int

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM tasks WHERE column_id = :columnId")
    suspend fun deleteAllFromColumn(columnId: Long): Int

    // endregion

    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE column_id = :columnId")
    suspend fun count(columnId: Long): Int
}