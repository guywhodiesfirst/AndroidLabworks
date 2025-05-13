package com.example.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class StepsDatabaseHandler(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "steps.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "steps"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_STEPS = "steps"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_DATE TEXT PRIMARY KEY,
                $COLUMN_STEPS INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun saveSteps(date: String, steps: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, date)
            put(COLUMN_STEPS, steps)
        }
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
    }

    fun getAllSteps(): List<Pair<String, Int>> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_DATE DESC")
        val result = mutableListOf<Pair<String, Int>>()

        if (cursor.moveToFirst()) {
            do {
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val steps = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STEPS))
                result.add(Pair(date, steps))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return result
    }
}
