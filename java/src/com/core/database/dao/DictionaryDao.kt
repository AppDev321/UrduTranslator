package com.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.core.database.entity.DictionaryEntity
import com.core.database.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface DictionaryDao {

    @Query("Select * from a_Group")
    suspend fun getDictionaryData (): List<DictionaryEntity>


    @Query("Select * from a_Group ORDER BY RANDOM() LIMIT 3")
    suspend fun getRandomOption (): List<DictionaryEntity>

    @Query("Select * from a_Group ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord (): DictionaryEntity


}