package com.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


object DatabaseMigrations {
    const val DB_VERSION = 1

    val MIGRATIONS: Array<Migration>
        get() = arrayOf(
           // migration1To2(),
        )

    private fun migration1To2(): Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `history` (`title` TEXT NOT NULL, `created` INTEGER NOT NULL, PRIMARY KEY(`title`))")
        }
    }

}