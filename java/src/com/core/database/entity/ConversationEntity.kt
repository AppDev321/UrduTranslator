package com.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_conversation")
data class ConversationEntity(
    @PrimaryKey
    @ColumnInfo("id")
    var id: Long? = 0,
    @ColumnInfo("inputLangCode")
    var inputLangCode: String? = "",
    @ColumnInfo("inputText")
    var inputText: String? = "",
    @ColumnInfo("outputLangCode")
    var outputLangCode: String? = "",
    @ColumnInfo("outputText")
    var outputText: String? = "",
    @ColumnInfo("isRightSide")
    var isRightSide: Boolean? = false
)