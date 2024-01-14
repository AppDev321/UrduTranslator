package com.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tbl_records")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    var id: Long? = 0,

    @ColumnInfo(name = "translated_text")
    var translatedText: String? = "",

    @ColumnInfo(name = "text")
    var textForTranslation: String? = "",

    @ColumnInfo(name = "from_code")
    var fromCode: String? = "",

    @ColumnInfo(name = "to_code")
    var toCode: String? = "",

    @ColumnInfo(name = "from_lang")
    var fromLang: String? = "",

    @ColumnInfo(name = "to_lang")
    var toLang: String? = "",

    @ColumnInfo(name = "is_favorite")
    var isFav: Boolean? = false,

    @ColumnInfo(name = "is_history")
    var isHistory: Boolean? = false

): Serializable