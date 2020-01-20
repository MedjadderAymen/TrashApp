package com.medjay.trashapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.medjay.trashapp.entities.Client;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import androidx.annotation.Nullable;

public class MyHelperSQLite extends SQLiteOpenHelper {

    private final static String SQL_DataBase_name="trash.db";
    private final static int version=1;

    public MyHelperSQLite( Context context) {
        super(context, SQL_DataBase_name,null,version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table clients (id INTEGER ,last_name TEXT, first_name TEXT, password TEXT , birthdate DATE,android_version TEXT,phone_number Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists clients");
        onCreate(sqLiteDatabase);
    }

    public void CreateClient(Client client){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from clients");
        db.execSQL("insert into clients (id  ,last_name , first_name , password  , birthdate ,android_version ,phone_number ) values('"+client.getId_user()+"','"+client.getLast_name()+"','"+client.getFirst_name()+"','"+client.getPassword()+"','"+client.getBirthdate()+"','"+client.getAndroid_version()+"','"+client.getPhone_number()+"')");
    }

    public Client getClient(){
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery ="select * from clients";
        Cursor cursor=db.rawQuery(selectQuery, null);


        if(cursor!=null)
            cursor.moveToFirst();

        DateFormat df= DateFormat.getDateInstance(DateFormat.SHORT);
        Date birthday = null;
        try {
            birthday = df.parse(cursor.getString(cursor.getColumnIndex("birthdate")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Client client=new Client(cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("last_name"))+"",
                cursor.getString(cursor.getColumnIndex("first_name"))+"",
                cursor.getString(cursor.getColumnIndex("last_name"))+" "+cursor.getString(cursor.getColumnIndex("first_name")),
                cursor.getString(cursor.getColumnIndex("password"))+"",
                 birthday,
                cursor.getString(cursor.getColumnIndex("android_version"))+"",
                cursor.getString(cursor.getColumnIndex("phone_number"))+"");

        return client;
    }
}
