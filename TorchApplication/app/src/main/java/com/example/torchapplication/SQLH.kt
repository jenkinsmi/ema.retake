package com.example.torchapplication

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.Context

// SQLite database.

class SQLH(ctx:Context) : SQLiteOpenHelper(ctx,"Settings", null, 1) {

// Create table on device.

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL ("CREATE TABLE IF NOT EXISTS LuxSettings (ID INTEGER PRIMARY KEY, lightON INTEGER, lightOFF INTEGER)")
    }

    override fun onUpgrade(db:SQLiteDatabase, oldVersion:Int, newVersion:Int) {
        db.execSQL ("DROP TABLE IF EXISTS LuxSettings")
        onCreate(db)
    }

// Insert record to SQLite database.

    fun insertRecord(lightON : Long, lightOFF : Long) : Long{
        delete()
        val db = getWritableDatabase()
        val stmt = db.compileStatement ("INSERT INTO LuxSettings(ID,lightON, lightOFF) VALUES (?, ?, ?)");
        stmt.bindLong (1, 1)
        stmt.bindLong (2, lightON)
        stmt.bindLong (3, lightOFF)
        val id = stmt.executeInsert()
        return id
    }

// Delete record from SQLite database.

    fun delete(){
        val db = getWritableDatabase()
        val stmt = db.compileStatement  ("DELETE FROM LuxSettings WHERE ID=?");
        stmt.bindLong (1, 1)
        val n = stmt.executeUpdateDelete()

    }

// Find upper values for turning on torch.

    fun OnSettings() : Long {
        val db = getReadableDatabase()
        val cursor = db.rawQuery ("SELECT * FROM LuxSettings WHERE ID=1",null )
        if (cursor.moveToFirst()){
            val r = cursor.getLong(cursor.getColumnIndex("lightON"))
            cursor.close()
            return r
        }
        cursor.close()
        return 1
    }

// Find lower values for turning off torch.

    fun OffSettings() : Long {
        val db = getReadableDatabase()
        val cursor = db.rawQuery ("SELECT * FROM LuxSettings WHERE ID=1",null )
        if (cursor.moveToFirst()){
            val r = cursor.getLong(cursor.getColumnIndex("lightOFF"))
            cursor.close()
            return r
        }
        cursor.close()
        return 100
    }
}