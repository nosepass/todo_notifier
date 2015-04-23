package com.github.nosepass.todonotifier.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.nosepass.todonotifier.kaffeine.v
import nl.qbusict.cupboard.CupboardFactory.cupboard


public class CupboardSQLiteOpenHelper(c: Context) : SQLiteOpenHelper(c, "todopref.db", null, 1) {
    override fun onConfigure(db: SQLiteDatabase) {
        v("onConfigure")
        cupboard().register(javaClass<TodoPrefData>())
    }

    override fun onCreate(db: SQLiteDatabase) {
        cupboard().withDatabase(db).createTables()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        cupboard().withDatabase(db).upgradeTables()
    }
}
