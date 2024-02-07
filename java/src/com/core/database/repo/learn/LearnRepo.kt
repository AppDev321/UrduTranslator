package com.core.database.repo.learn

import com.core.database.entity.LearnEntity

interface LearnRepo {
    suspend fun getCategoryDetailList(name: String): List<LearnEntity>
    suspend fun getDetailBySubCategory(name:String): List<LearnEntity>
    suspend fun getConversations(): List<LearnEntity>
    suspend fun getGrammars(): List<LearnEntity>

    suspend fun getPhrases():List<LearnEntity>
    suspend fun getWords():List<LearnEntity>
    suspend fun getTenses():List<LearnEntity>
}