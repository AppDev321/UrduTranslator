package com.core.database.repo.dictionary

import com.core.database.dao.DictionaryDao
import com.core.database.entity.DictionaryEntity
import com.core.extensions.safeGet
import javax.inject.Inject

class DictionaryRepoImpl @Inject constructor(
    private val dictionaryDao: DictionaryDao
) : DictionaryRepo {

    override suspend fun getDictionaryDataList(): List<DictionaryEntity> {
        return dictionaryDao.getDictionaryData()
    }

    override suspend fun getDictionaryRandomOptions(): List<String> {
        val list = dictionaryDao.getRandomOption()
        val listStrings = arrayListOf<String>()
        list.map {
            listStrings.add(it.word.safeGet())
        }
        return listStrings
    }

    override suspend fun getDictionaryRandomWord(): List<String> {
        return arrayListOf(
            dictionaryDao.getRandomWord().meaning.safeGet(),
            dictionaryDao.getRandomWord().word.safeGet(),
        )
    }

    override suspend fun getDictionaryRandomDictionaryObject(): DictionaryEntity {
        return dictionaryDao.getRandomWord()
    }
}