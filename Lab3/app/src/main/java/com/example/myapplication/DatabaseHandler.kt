package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DB_NAME = "AppDatabase.db"

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE Students (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "year INTEGER," +
            "faculty TEXT)"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS Students")
        onCreate(db)
    }

    fun insertStudent(student: Student): Boolean {
        val db = this.writableDatabase
        val contentValue = ContentValues().apply {
            put("year", student.year)
            put("faculty", student.faculty)
        }
        val result = db.insert("Students", null, contentValue)
        return result != -1L
    }

    fun getAllStudents(): List<Student> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Students", null)
        val result = mutableListOf<Student>()

        if(cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val year = cursor.getInt(cursor.getColumnIndexOrThrow("year"))
                val faculty = cursor.getString(cursor.getColumnIndexOrThrow("faculty"))

                result.add(Student(id, faculty, year))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return result
    }
}