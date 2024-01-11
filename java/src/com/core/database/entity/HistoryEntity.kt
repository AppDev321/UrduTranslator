package com.core.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_records")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    var id: Long? = 0,

    @ColumnInfo(name = "urdu_word")
    var translatedText: String? = "",

    @ColumnInfo(name = "eng_word")
    var textForTranslation: String? = "",

    @ColumnInfo(name = "is_urdu")
    var isUrdu: Boolean? = false,

    @ColumnInfo(name = "is_favorite")
    var isFav: Boolean? = false,

    @ColumnInfo(name = "is_history")
    var isHistory: Boolean? = false

): Parcelable