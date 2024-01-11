package com.core.database.repo.history

import com.core.database.entity.HistoryEntity

interface HistoryRepo {
    suspend fun getHistoryDataList(): List<HistoryEntity>
    suspend fun insertHistoryData(entity: HistoryEntity)
    suspend fun deleteHistoryData(id:Int)
    suspend fun getMaxRowID():Long
}