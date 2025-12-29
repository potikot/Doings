package com.potikot.doings.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.potikot.doings.data.data_source.ProjectWithBoards
import com.potikot.doings.data.data_source.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    // region Insert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: ProjectEntity): Long

    // endregion

    // region Update

    @Update
    suspend fun update(project: ProjectEntity): Int

    @Query("UPDATE projects SET selected_board_id = :boardId WHERE id = :id")
    suspend fun updateSelectedBoard(id: Long, boardId: Long?): Int

    @Query("UPDATE projects SET name = :name WHERE id = :id")
    suspend fun updateName(id: Long, name: String): Int

    // endregion

    // region Get

    @Transaction
    @Query("SELECT * FROM projects WHERE id = :id")
    fun getById(id: Long): Flow<ProjectWithBoards?>

    @Transaction
    @Query("SELECT * FROM projects WHERE account_id = :accountId")
    fun getAllFromAccount(accountId: Long): Flow<List<ProjectWithBoards>>

    // endregion

    // region Delete

    @Delete
    suspend fun delete(project: ProjectEntity): Int

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM projects WHERE account_id = :accountId")
    suspend fun deleteAllFromAccount(accountId: Long): Int

    // endregion
}