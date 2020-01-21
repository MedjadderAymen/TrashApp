package com.medjay.trashapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.ChallengeSQL;
import com.medjay.trashapp.entities.Client;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;

public class MyHelperSQLite extends SQLiteOpenHelper {

    private final static String SQL_DataBase_name="trash.db";
    private final static int version=1;

    public MyHelperSQLite( Context context) {
        super(context, SQL_DataBase_name,null,version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table challenges (id_challenge INTEGER ,creation_date Date, state INTEGER, starting_date DATE , ending_date DATE,latitude FLOAT,longitude float, street TEXT, city TEXT, zip_code TEXT , country Text,user_name TEXT,one_path TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists challenges");
        onCreate(sqLiteDatabase);
    }

    public void CreateChallenge(ChallengeSQL challenge){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("insert into challenges (id_challenge ,creation_date , state , starting_date  , ending_date ,latitude ,longitude,street,city,zip_code,country,user_name,one_path ) values('"+challenge.getId_challenge()+"','"+challenge.getCreation_date()+"','"+challenge.getState()+"','"+challenge.getStarting_date()+"','"+challenge.getEnding_date()+"','"+challenge.getLatitude()+"','"+challenge.getLongitude()+"','"+challenge.getStreet()+"','"+challenge.getCity()+"','"+challenge.getZip_code()+"','"+challenge.getCountry()+"','"+challenge.getUser_name()+"','"+challenge.getOne_path()+"')");
    }

    public void DeleteChallenges(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from challenges");
    }

    public List<ChallengeSQL> getChallenges(){
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery ="select * from challenges";
        Cursor cursor=db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        List<ChallengeSQL> challenges=new ArrayList<>();

        DateFormat df= DateFormat.getDateInstance(DateFormat.SHORT);
        Date birthday = null;
        try {
            birthday = df.parse("2020-02-22");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        while (!cursor.isAfterLast()) {
            challenges.add(new ChallengeSQL(cursor.getInt(cursor.getColumnIndex("id_challenge")),
                    birthday,
                    cursor.getInt(cursor.getColumnIndex("state")),
                    birthday,
                    birthday,
                    cursor.getFloat(cursor.getColumnIndex("latitude")),
                    cursor.getFloat(cursor.getColumnIndex("longitude")),
                    cursor.getString(cursor.getColumnIndex("street")) + "",
                    cursor.getString(cursor.getColumnIndex("city")) + "",
                    cursor.getString(cursor.getColumnIndex("zip_code")) + "",
                    cursor.getString(cursor.getColumnIndex("country")) + "",
                    cursor.getString(cursor.getColumnIndex("user_name")) + "",
                    cursor.getString(cursor.getColumnIndex("one_path")) + ""));

            cursor.moveToNext();
        }
        return challenges;
    }
}
