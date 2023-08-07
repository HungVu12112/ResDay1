package com.thinkdiffai.cloud_note.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thinkdiffai.cloud_note.Model.Setting_Sort;
import com.thinkdiffai.cloud_note.database.Database;

public class Sort {
    Database db ;
    SQLiteDatabase database;
    public Sort(Context context){
        db= new Database(context);
        database = db.getWritableDatabase();
    }
    public void close(){
        database.close();
    }
    public Setting_Sort getNameSort(){
        Setting_Sort obj = new Setting_Sort();
        String select ="select * from Sort";
        Cursor cursor = database.rawQuery(select, null);
        if(cursor!=null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                obj.setId(cursor.getInt(0));
                obj.setSortName(cursor.getString(1));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return  obj;
    }
    public long insert(Setting_Sort obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("sort", obj.getSortName());
        return database.insert("Sort", null, contentValues);
    }
    public  int update(Setting_Sort obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("sort", obj.getSortName());
        return database.update("Sort", contentValues, "id=?", new String[]{obj.getId()+""});
    }




}
