package com.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.core.database.entity.LearnEntity


@Dao
interface LearnDao {

    @Query("SELECT * FROM english_data group by category_name")
    suspend fun getCategoryList(): List<LearnEntity>

    @Query("SELECT * FROM english_data where subCategory = :name")
    suspend fun getDetailBySubCategory(name: String): List<LearnEntity>


    @Query("SELECT * FROM english_data  where category_name in (\"Conversations\") GROUP by subCategory")
    suspend fun getConversations(): List<LearnEntity>
}