package com.core.database.repo.learn

import com.core.database.dao.LearnDao
import com.core.database.entity.LearnEntity
import javax.inject.Inject

class LearnRepoImpl @Inject constructor(
    private val learnDao: LearnDao
) : LearnRepo {
    override suspend fun getCategoryList(): List<LearnEntity> {
        return learnDao.getCategoryList()
    }

    override suspend fun getDetailBySubCategory(name: String): List<LearnEntity> {
        return learnDao.getDetailBySubCategory(name)
    }

    override suspend fun getConversations(): List<LearnEntity> {
        return learnDao.getConversations()
    }
}