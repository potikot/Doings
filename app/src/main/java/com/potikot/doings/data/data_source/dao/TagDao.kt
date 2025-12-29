package com.potikot.doings.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.potikot.doings.data.data_source.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    // region Insert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: TagEntity): Long

    // endregion

    // region Update

    @Update
    suspend fun update(tag: TagEntity): Int

    @Query("UPDATE task_tags SET position = :position WHERE id = :id")
    suspend fun updatePosition(id: Long, position: Int): Int

    // endregion

    // region Get

    @Query("SELECT * FROM task_tags WHERE id = :id")
    fun getById(id: Long): Flow<TagEntity?>

    @Query("SELECT * FROM task_tags WHERE task_id = :taskId")
    fun getAllFromTask(taskId: Long): Flow<List<TagEntity>>

    // endregion

    // region Delete

    @Delete
    suspend fun delete(tag: TagEntity): Int

    @Query("DELETE FROM task_tags WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM task_tags WHERE task_id = :taskId")
    suspend fun deleteAllFromTask(taskId: Long): Int

    // endregion
}