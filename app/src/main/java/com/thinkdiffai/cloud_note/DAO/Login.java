package com.thinkdiffai.cloud_note.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thinkdiffai.cloud_note.Model.Model_State_Login;
import com.thinkdiffai.cloud_note.database.Database;

public class Login {
    Database database ;
    SQLiteDatabase db;
    public Login(Context context){
        database = new Database(context);
        db = database.getWritableDatabase();
    }
    public void close(){
        db.close();
    }
    public Model_State_Login getLogin(){
        Model_State_Login obj = new Model_State_Login();
        String select ="select * from Login ";
        Cursor cursor = db.rawQuery(select, null);
        if(cursor!=null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                obj.setIdUer(cursor.getInt(0));
                obj.setJwt(cursor.getString(1));
                obj.setState(cursor.getInt(2));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return  obj;
    }
    public long insert(Model_State_Login obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("idUSER", obj.getIdUer());
        contentValues.put("jwt", obj.getJwt());
        contentValues.put("state", obj.getState());
        return db.insert("Login", null, contentValues);
    }
    public int update(Model_State_Login obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", obj.getState());
        return db.update("Login", contentValues, "idUSER=?", new String[]{obj.getIdUer()+""});
    }

    public int delete(Model_State_Login obj){
        return db.delete("Login","idUSER=?", new String[]{obj.getIdUer()+""});
    }


}
