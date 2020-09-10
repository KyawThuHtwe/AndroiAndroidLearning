package com.cu.android.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME= "Favorite.db";
    private static final int DATABASE_VERSION=1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Link (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,LINK TEXT,FAVORITE TEXT,CATEGORY TEXT)");
        db.execSQL("CREATE TABLE Note (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,LINK TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Link");
        db.execSQL("DROP TABLE IF EXISTS Note");
    }

    public boolean insertLink(String name,String link,String favorite,String category){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("NAME",name);
        contentValues.put("LINK",link);
        contentValues.put("FAVORITE",favorite);
        contentValues.put("CATEGORY",category);

        long result=db.insert("Link",null,contentValues);
        db.close();

        if(result==-1){
            return false;
        }else {
            return true;
        }

    }
    public Cursor getLink(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("Select * from Link",null);
        return res;
    }
    public boolean updateLink(String id,String favorite){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("FAVORITE",favorite);
        int result=database.update("Link",contentValues,"ID=?",new String[]{id});
        if(result>0){
            return true;
        }else {
            return false;
        }
    }
    public boolean insertNote(String name,String link){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("NAME",name);
        contentValues.put("LINK",link);
        long result=db.insert("Note",null,contentValues);
        db.close();

        if(result==-1){
            return false;
        }else {
            return true;
        }

    }
    public Cursor getNote(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("Select * from Note",null);
        return res;
    }
    public int deleteNote(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int res=db.delete("Note","ID=?",new String[]{id});
        return res;
    }
}
