package com.core.database.repo.history

import com.core.database.dao.HistoryDao
import com.core.database.entity.HistoryEntity
import javax.inject.Inject

class HistoryRepoImpl @Inject constructor(
    private val historyDao: HistoryDao
) : HistoryRepo {


    override suspend fun getHistoryDataList(): List<HistoryEntity> {
        return historyDao.getHistoryList()
    }

    override suspend fun getFavouriteDataList(): List<HistoryEntity> {
        return historyDao.getFavouriteList()
    }

    override suspend fun insertHistoryData(entity: HistoryEntity) {
        historyDao.insertHistory(entity)
    }

    override suspend fun deleteHistoryData(id: Int) {
        historyDao.deleteHistory(id)
    }

    override suspend fun getMaxRowID(): Long {
        val id = historyDao.getMaxRowId()?:0
        return  id + 1
    }

    override suspend fun getLastRecord(): HistoryEntity {
        return historyDao.getLatestRecord()
    }
}