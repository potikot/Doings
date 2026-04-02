package com.potikot.doings.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.potikot.doings.data.data_source.AccountWithProjects
import com.potikot.doings.data.data_source.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    // region Insert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity): Long

    // endregion

    // region Update

    @Update
    suspend fun update(account: AccountEntity): Int

    // endregion

    // region Get

    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :id")
    fun getById(id: Long): Flow<AccountWithProjects?>

    @Transaction
    @Query("SELECT * FROM accounts WHERE external_id = :id")
    fun getByExternalId(id: String): Flow<List<AccountWithProjects>>

    @Transaction
    @Query("SELECT * FROM accounts")
    fun getAll(): Flow<List<AccountWithProjects>>

    // endregion

    // region Delete

    @Delete
    suspend fun delete(account: AccountEntity): Int

    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM accounts")
    suspend fun deleteAll()

    // endregion

    @Query("SELECT COUNT(*) FROM accounts")
    fun count(): Int
}