package pl.hoorder.myapplication1

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "pogrzebowka.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableFuneral = "CREATE TABLE funeral(" +
                "id_funeral INTEGER PRIMARY KEY," +
                "locality TEXT," +
                "date TEXT," +
                "amount INTEGER)"
        db?.execSQL(createTableFuneral)

        val createTableUsers = "CREATE TABLE users(" +
                "id_user INTEGER PRIMARY KEY," +
                "login TEXT," +
                "password TEXT," +
                "userName TEXT)"
        db?.execSQL(createTableUsers)
    }
    override fun onUpgrade(db: SQLiteDatabase?,  oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS funeral")
        db?.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun searchLocality(locality: String): List<String> {
        val recordsList = ArrayList<String>()
        val db = this.readableDatabase

        val selectQuery = "SELECT locality, date FROM funeral WHERE locality = '$locality' ORDER BY date DESC"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val column1 = cursor.getString(cursor.getColumnIndexOrThrow("locality"))
                val column2 = cursor.getString(cursor.getColumnIndexOrThrow("date"))

                val record = "$column1 - $column2"
                recordsList.add(record)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return recordsList
    }

    fun displayStatistic(): List<String> {
        val recordsList = ArrayList<String>()
        val db = this.readableDatabase
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val selectQuery = "SELECT locality, strftime('%Y', date) AS Rok, COUNT(locality) AS 'Liczba imprez' FROM funeral WHERE strftime('%Y', date) = '$year' GROUP BY locality ORDER BY COUNT(locality) DESC"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val column1 = cursor.getString(cursor.getColumnIndexOrThrow("locality"))
                val column2 = cursor.getString(cursor.getColumnIndexOrThrow("Liczba imprez"))

                val record = "$column1 - $column2"
                recordsList.add(record)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return recordsList
    }

    fun funeralsToPay(): List<String> {
        val recordsList = ArrayList<String>()
        val db = this.readableDatabase

        val selectQuery = "SELECT * FROM funeral WHERE amount >= 75 ORDER BY id_funeral DESC"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val column1 = cursor.getString(cursor.getColumnIndexOrThrow("id_funeral"))
                val column2 = cursor.getString(cursor.getColumnIndexOrThrow("locality"))
                val column3 = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val column4 = cursor.getString(cursor.getColumnIndexOrThrow("amount"))

                val record = "$column1 - $column2 - $column3 - $column4 zł"
                recordsList.add(record)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return recordsList
    }

    fun show30DaysFuneral(): List<String> {
        val recordsList = ArrayList<String>()
        val db = this.readableDatabase

        val selectQuery = "SELECT id_funeral, locality, date, amount FROM funeral WHERE date BETWEEN date('now', '-30 days') AND date('now') ORDER BY id_funeral DESC"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val column1 = cursor.getString(cursor.getColumnIndexOrThrow("id_funeral"))
                val column2 = cursor.getString(cursor.getColumnIndexOrThrow("locality"))
                val column3 = cursor.getString(cursor.getColumnIndexOrThrow("date"))

                val record = "$column1 - $column2 - $column3"
                recordsList.add(record)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return recordsList
    }




}
