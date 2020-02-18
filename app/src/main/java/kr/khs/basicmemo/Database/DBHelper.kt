package kr.khs.basicmemo.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kr.khs.basicmemo.RV.MemoData

class DBHelper(context : Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private val DB_NAME = "MEMO_DB"
        private val DB_VERSION = 1
        private val TABLE_NAME = "MEMO"
        private val SEQ = "seq"
        private val DATE = "write_date"
        private val TEXT = "text"
        private val MODIFY_YN = "modify_yn" //I -> not delete, D -> delete
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME" +
                "($SEQ Integer PRIMARY KEY AUTOINCREMENT," +
                "$DATE TEXT," +
                "$TEXT TEXT," +
                "$MODIFY_YN TEXT)"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun addMemo(memo : MemoData) : Boolean{
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(DATE, memo.date)
        values.put(TEXT, memo.text)
        values.put(MODIFY_YN, "I")

        val _success = db.insert(TABLE_NAME, null, values)
        db.close()

        return (Integer.parseInt("$_success") != -1)
    }

    fun getMemo() : MutableList<MemoData> {
        val retval = mutableListOf<MemoData>()
        val db = readableDatabase
        val selectQuery = "SELECT * " +
                "FROM $TABLE_NAME " +
                "WHERE $MODIFY_YN <> 'D'"
        val cursor = db.rawQuery(selectQuery, null)
        var tempSeq = 0
        var tempDate = ""
        var tempText = ""

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    tempSeq = cursor.getInt(cursor.getColumnIndex(SEQ))
                    tempDate = cursor.getString(cursor.getColumnIndex(DATE))
                    tempText = cursor.getString(cursor.getColumnIndex(TEXT))
                    retval.add(MemoData(tempSeq, tempDate, tempText))
                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return retval
    }

    fun updateMemo(memo : MemoData) : Boolean{
        val db = writableDatabase
        val values = ContentValues()
        values.put(DATE, memo.date)
        values.put(TEXT, memo.text)

        val _success = db.update(TABLE_NAME, values, "seq=?", arrayOf(memo.seq.toString()))
        db.close()

        return (_success != -1)
    }

    fun deleteMemo(memo : MemoData) : Boolean{
        val db = writableDatabase
        val values = ContentValues()
        values.put(MODIFY_YN, "D")

        val _success = db.update(TABLE_NAME, values, "seq=?", arrayOf(memo.seq.toString()))
        db.close()

        return (_success != -1)
    }
}