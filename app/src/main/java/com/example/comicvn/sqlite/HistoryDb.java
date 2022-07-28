package com.example.comicvn.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HistoryDb extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String HISTORY_DB_NAME = "history.db";
    public static final String TABLE_HISTORY = "history";

    public HistoryDb(Context context) {
        super(context, HISTORY_DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_HISTORY + "(" +
                "id TEXT primary key," +
                "comicId TEXT," +
                "chapterId TEXT,"+
                "dateUpdate TEXT" +
                ")";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public int insert(String comicId, String chapterId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", String.valueOf(System.currentTimeMillis()));
        values.put("comicId", comicId);
        values.put("chapterId", chapterId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put("dateUpdate", dateFormat.format(Calendar.getInstance().getTime()));
        return (int) db.insert(TABLE_HISTORY, null, values);
    }

    public Map<String, String> getHistory(){
        SQLiteDatabase db = getReadableDatabase();
        String table = TABLE_HISTORY;
        String[] columns = new String[] {"comicId", "chapterId"};
        String selection = null;
        String[] arguments = null;
        String groupBy = "comicId";
        String having = "dateUpdate = MAX(dateUpdate)";
        String orderBy = null;
        Cursor cursor = db.query(table, columns, selection, arguments, groupBy, having, orderBy);
        cursor.moveToFirst();
        Map<String, String> history = new HashMap<>();
        while(!cursor.isAfterLast()){
            history.put(cursor.getString(0), cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return history;
    }

    public int delete(String comicId){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_HISTORY, "comicId = ?", new String[] {comicId});
    }
}
