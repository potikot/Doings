package com.potikot.doings.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.potikot.doings.data.data_source.ColumnWithTasks
import com.potikot.doings.data.data_source.entity.ColumnEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ColumnDao {
    // region Insert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(column: ColumnEntity): Long

    // endregion

    // region Update

    @Update
    suspend fun update(column: ColumnEntity): Int

    @Query("UPDATE columns SET position = :position WHERE id = :id")
    suspend fun updatePosition(id: Long, position: Int): Int

    // endregion

    // region Get

    @Transaction
    @Query("SELECT * FROM columns WHERE id = :id")
    fun getById(id: Long): Flow<ColumnWithTasks?>

    @Transaction
    @Query("SELECT * FROM columns WHERE board_id = :boardId")
    fun getAllFromBoard(boardId: Long): Flow<List<ColumnWithTasks>>

    // endregion

    // region Delete

    @Delete
    suspend fun delete(column: ColumnEntity): Int

    @Query("DELETE FROM columns WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM columns WHERE board_id = :boardId")
    suspend fun deleteAllFromBoard(boardId: Long): Int

    // endregion
}