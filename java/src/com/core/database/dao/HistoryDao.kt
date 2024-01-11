package com.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.core.database.entity.HistoryEntity


@Dao
interface HistoryDao {

    @Insert()
    suspend fun insertHistory(data: HistoryEntity)

    @Query("Delete from tbl_records Where rowid = :id")
    suspend fun deleteHistory(id: Int)

    @Query("Select * from tbl_records")
    suspend fun getHistoryList(): List<HistoryEntity>

    @Query("Select max(rowid) from tbl_records")
    suspend fun getMaxRowId(): Long

}