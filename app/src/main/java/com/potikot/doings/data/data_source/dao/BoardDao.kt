package com.potikot.doings.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.potikot.doings.data.data_source.BoardWithColumns
import com.potikot.doings.data.data_source.entity.BoardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardDao {
    // region Insert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(board: BoardEntity): Long

    // endregion

    // region Update

    @Update
    suspend fun update(board: BoardEntity): Int

    @Query("UPDATE boards SET position = :position WHERE id = :id")
    suspend fun updatePosition(id: Long, position: Int): Int

    @Query("UPDATE boards SET selected_column_id = :columnId WHERE id = :id")
    suspend fun updateSelectedColumn(id: Long, columnId: Long?): Int

    // endregion

    // region Get

    @Transaction
    @Query("SELECT * FROM boards WHERE id = :id")
    fun getById(id: Long): Flow<BoardWithColumns?>

    @Transaction
    @Query("SELECT * FROM boards WHERE project_id = :projectId")
    fun getAllFromProject(projectId: Long): Flow<List<BoardWithColumns>>

    // endregion

    // region Delete

    @Delete
    suspend fun delete(board: BoardEntity): Int

    @Query("DELETE FROM boards WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM boards WHERE project_id = :projectId")
    suspend fun deleteAllFromProject(projectId: Long): Int

    // endregion
}