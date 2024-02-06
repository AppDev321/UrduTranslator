package com.core.database.repo.learn

import com.core.database.entity.LearnEntity

interface LearnRepo {
    suspend fun getCategoryList(): List<LearnEntity>
    suspend fun getDetailBySubCategory(name:String): List<LearnEntity>
    suspend fun getConversations(): List<LearnEntity>
}