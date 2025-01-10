package com.example.moodtracker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MoodDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "moodTracker.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "moods"
        private const val COLUMN_ID = "id"
        private const val COLUMN_MOOD = "mood"
        private const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_MOOD TEXT, " +
                "$COLUMN_TIMESTAMP TEXT)")
        db.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addMood(mood: String, timestamp: String) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_MOOD, mood)
        cv.put(COLUMN_TIMESTAMP, timestamp)
        db.insert(TABLE_NAME, null, cv)
        db.close()
    }

    fun getAllMoods(): List<Pair<String, String>> {
        val moodList = mutableListOf<Pair<String, String>>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_TIMESTAMP DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val mood = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOOD))
                val timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                moodList.add(Pair(mood, timestamp))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return moodList
    }
}
