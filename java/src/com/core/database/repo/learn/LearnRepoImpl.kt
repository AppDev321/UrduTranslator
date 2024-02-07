package com.core.database.repo.learn

import com.core.database.dao.LearnDao
import com.core.database.entity.LearnEntity
import javax.inject.Inject

class LearnRepoImpl @Inject constructor(
    private val learnDao: LearnDao
) : LearnRepo {
    override suspend fun getCategoryDetailList(name:String): List<LearnEntity> {
        return learnDao.getCategoryDetailList(name)
    }

    override suspend fun getDetailBySubCategory(name: String): List<LearnEntity> {
        return learnDao.getDetailBySubCategory(name)
    }

    override suspend fun getConversations(): List<LearnEntity> {
        return learnDao.getConversations()
    }

    override suspend fun getGrammars(): List<LearnEntity> {
       return learnDao.getGrammarList()
    }

    override suspend fun getPhrases(): List<LearnEntity> {
        return learnDao.getPhrases()
    }

    override suspend fun getWords(): List<LearnEntity> {
        return learnDao.getWords()
    }

    override suspend fun getTenses(): List<LearnEntity> {
        return learnDao.getTenses()
    }
}