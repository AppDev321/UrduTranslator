package com.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.core.database.entity.HistoryEntity


@Dao
interface HistoryDao {

    @Insert()
    suspend fun insertHistory(data: HistoryEntity)

    @Query("Delete from tbl_records Where rowid = :id")
    suspend fun deleteHistory(id: Int)

    @Query("Select * from tbl_records order by rowid desc")
    suspend fun getHistoryList(): List<HistoryEntity>

    @Query("Select * from tbl_records where is_favorite = 1 order by rowid desc")
    suspend fun getFavouriteList(): List<HistoryEntity>

    @Query("Select max(rowid) from tbl_records")
    suspend fun getMaxRowId(): Long?


    @Query("Select * from tbl_records order by rowid desc limit 1")
    suspend fun getLatestRecord(): HistoryEntity


    @Query("UPDATE tbl_records SET is_favorite = :isFav WHERE rowid = :rowID")
    suspend fun updateFav(isFav: Boolean, rowID: Int): Int
}