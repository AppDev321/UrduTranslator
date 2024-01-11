package com.core.database.repo.dictionary

import com.core.database.dao.DictionaryDao
import com.core.database.entity.DictionaryEntity
import javax.inject.Inject

class DictionaryRepoImpl @Inject constructor(
    private val dictionaryDao: DictionaryDao
) : DictionaryRepo {

    override suspend fun getDictionaryDataList(): List<DictionaryEntity> {
       return dictionaryDao.getDictionaryData()
    }
}