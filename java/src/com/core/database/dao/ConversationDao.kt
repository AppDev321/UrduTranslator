package com.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.core.database.entity.ConversationEntity


@Dao
interface ConversationDao {

    @Insert()
    suspend fun insertConversation(data: ConversationEntity)

    @Query("Delete from tbl_conversation Where id = :rowID")
    suspend fun deleteConversation(rowID: Int)

    @Query("Select * from tbl_conversation order by id asc")
    suspend fun getConversationList(): List<ConversationEntity>

    @Query("Select max(rowid) from tbl_conversation")
    suspend fun getMaxRowId(): Long?

    @Query("Select * from tbl_conversation order by id desc limit 1")
    suspend fun getLatestRecord(): ConversationEntity

}