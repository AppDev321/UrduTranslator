package com.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.core.database.entity.LearnEntity


@Dao
interface LearnDao {

    @Query("SELECT * FROM english_data where category_name = :name")
    suspend fun getCategoryDetailList(name: String): List<LearnEntity>

    @Query("SELECT * FROM english_data where subCategory = :name")
    suspend fun getDetailBySubCategory(name: String): List<LearnEntity>


    @Query("SELECT * FROM english_data  where category_name in (\"Conversations\") GROUP by subCategory")
    suspend fun getConversations(): List<LearnEntity>

    @Query("SELECT * FROM english_data where subCategory in (\"Function or Job\") GROUP by category_name ")
    suspend fun getGrammarList(): List<LearnEntity>


    @Query("SELECT * FROM english_data where category_name in (\"Phrases\") GROUP by  subCategory")
    suspend fun getPhrases(): List<LearnEntity>

    @Query("SELECT * FROM english_data where category_name in (\"vocabulary1\") GROUP by subCategory")
    suspend fun getWords(): List<LearnEntity>


    @Query("SELECT * FROM english_data   where subCategory in (\"Definition\") GROUP by  category_name")
    suspend fun getTenses(): List<LearnEntity>
}