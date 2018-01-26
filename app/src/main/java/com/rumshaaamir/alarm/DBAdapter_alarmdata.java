package com.rumshaaamir.alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class DBAdapter_alarmdata {

    //database tag
    static final String TAG="DBAdapter_alarmdata";

    //database version
    static final int DATABASE_VERSION=20;

    //database name
    static final String DATABASE_NAME="DataBase_alarmData";

    //database tableName
    static final String DATABASE_TABLE_AlarmBasic="AlarmBasic";
    static final String DATABASE_TABLE_AlarmDays="AlarmDays";
    static final String DATABASE_TABLE_AlarmTime="AlarmTime";
    static final String DATABASE_TABLE_AlramDate="AlarmDate";
    static final String DATABASE_TABLE_AlarmSnooze="AlarmSnooze";

    //AlarmBasic attribute
    static final String id_ab="id";
    static final String onOff_ab="onOff";
    static final String type_ab="type";
    static final String path_ab="path";
    static final String title_ab="title";
    static final String status_ab="status";

    //AlarmDays attributes
    static final String id_ad="id";
    static final String mon_ad="monday";
    static final String tue_ad="tuesday";
    static final String wed_ad="wednesday";
    static final String thur_ad="thursday";
    static final String fri_ad="friday";
    static final String sat_ad="saturday";
    static final String sun_ad="sunday";
    static final String status_ad="status";

    //AlarmTime attributes
    static final String id_at="id";
    static final String hour_at="time";
    static final String min_at="minute";
    static final String status_at="status";

    //AlarmDate attributes
    static final String id_adt="id";
    static final String day_adt="day";
    static final String month_adt="month";
    static final String year_adt="year";
    static final String status_adt="status";

    //AlarmSnooze attributes
    static final String id_as="id";
    static final String noTime_as="noTime";
    static final String duration_as="duration";
    static final String status_as="status";

    //AlarmBasic create table
    static final String CREATETABLE_AlarmBaics="create table " + DATABASE_TABLE_AlarmBasic + "(" +
            id_ab + " integer," + onOff_ab + " integer," + type_ab +" text not null," + path_ab + " text not null," +
            title_ab + " text not null," + status_ab + " integer" + ")";
    /*static final String CREATETABLE_AlarmBaics="create table AlarmBasic (id integer, onOff integer, type text not null, " +
            "path text not null, title text not null, status integer);";*/


    //AlarmDays create table
    static final String CREATETABLE_AlarmDays="create table " + DATABASE_TABLE_AlarmDays + "(" +
            id_ad + " integer," + mon_ad + " text not null," + tue_ad + " text not null," + wed_ad + " text not null," +
            thur_ad + " text not null," + fri_ad + " text not null," + sat_ad + " text not null," +
            sun_ad + " text not null," + status_ad + " integer" + ")";


    //AlarmTime create table
    static final String CREATETABLE_AlarmTime="create table " + DATABASE_TABLE_AlarmTime + "(" +
            id_at + " integer," + hour_at + " integer," + min_at + " integer," + status_at + " integer" + ")";


    //AlarmDate create table
    static final String CREATETABLE_AlarmDate="create table " + DATABASE_TABLE_AlramDate + "(" +
            id_adt + " integer," + day_adt + " integer," + month_adt + " integer," + year_adt + " integer," +
            status_adt + " integer" + ")";


    //AlarmSnooze create table
    static final String CREATETABLE_AlarmSnooze="create table " + DATABASE_TABLE_AlarmSnooze + "(" +
            id_as + " integer," + duration_as + " integer," + noTime_as + " integer," + status_as + " integer" + ")";


    static final String query ="SELECT "+DATABASE_TABLE_AlarmBasic+"."+id_ab+" ,"+DATABASE_TABLE_AlarmBasic+"."+onOff_ab+", "+
            DATABASE_TABLE_AlarmBasic+"."+type_ab+", "+
            DATABASE_TABLE_AlarmBasic+"."+path_ab+", "+DATABASE_TABLE_AlarmBasic+"."+title_ab+", "+
            DATABASE_TABLE_AlarmTime+"."+hour_at+", "+DATABASE_TABLE_AlarmTime+"."+min_at+", "+
            DATABASE_TABLE_AlramDate+"."+day_adt+", "+DATABASE_TABLE_AlramDate+"."+month_adt+", "+
            DATABASE_TABLE_AlramDate+"."+year_adt+", "+
            DATABASE_TABLE_AlarmSnooze+"."+duration_as+", "+DATABASE_TABLE_AlarmSnooze+"."+noTime_as+", "+
            DATABASE_TABLE_AlarmDays+"."+mon_ad+", "+DATABASE_TABLE_AlarmDays+"."+tue_ad+", "+
            DATABASE_TABLE_AlarmDays+"."+wed_ad+", "+DATABASE_TABLE_AlarmDays+"."+thur_ad+", "+
            DATABASE_TABLE_AlarmDays+"."+fri_ad+", "+DATABASE_TABLE_AlarmDays+"."+sat_ad+", "+
            DATABASE_TABLE_AlarmDays+"."+sun_ad+" FROM "+DATABASE_TABLE_AlarmBasic+" INNER JOIN "+DATABASE_TABLE_AlarmTime+
            " ON "+DATABASE_TABLE_AlarmBasic+"."+id_ab+"="+DATABASE_TABLE_AlarmTime+"."+id_at+
            " INNER JOIN "+DATABASE_TABLE_AlramDate+" ON "+DATABASE_TABLE_AlramDate+"."+id_adt+"="+DATABASE_TABLE_AlarmTime+"."+
            id_at+" INNER JOIN "+DATABASE_TABLE_AlarmSnooze+" ON "+DATABASE_TABLE_AlarmSnooze+"."+id_as+"="+
            DATABASE_TABLE_AlramDate+"."+id_adt+" INNER JOIN "+DATABASE_TABLE_AlarmDays+" ON "+
            DATABASE_TABLE_AlarmDays+"."+id_adt+"="+DATABASE_TABLE_AlarmSnooze+"."+id_as;




    //context
    final Context context;

    DatabaseHelper databaseHelper;

    SQLiteDatabase sqLiteDatabase;

    public DBAdapter_alarmdata(Context context) {
        this.context=context;
        databaseHelper=new DatabaseHelper(this.context);
    }



    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            try {

                sqLiteDatabase.execSQL(CREATETABLE_AlarmBaics);
                sqLiteDatabase.execSQL(CREATETABLE_AlarmDays);
                sqLiteDatabase.execSQL(CREATETABLE_AlarmSnooze);
                sqLiteDatabase.execSQL(CREATETABLE_AlarmDate);
                sqLiteDatabase.execSQL(CREATETABLE_AlarmTime);

            }catch (SQLiteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("drop table if exists "+DATABASE_TABLE_AlarmBasic);
            sqLiteDatabase.execSQL("drop table if exists "+DATABASE_TABLE_AlarmDays);
            sqLiteDatabase.execSQL("drop table if exists "+DATABASE_TABLE_AlarmSnooze);
            sqLiteDatabase.execSQL("drop table if exists "+DATABASE_TABLE_AlramDate);
            sqLiteDatabase.execSQL("drop table if exists "+DATABASE_TABLE_AlarmTime);
            onCreate(sqLiteDatabase);
        }
    }

    //open database
    public DBAdapter_alarmdata open() throws SQLiteException {
        sqLiteDatabase=databaseHelper.getWritableDatabase();
        return this;
    }

    //close database
    public void close() {
        databaseHelper.close();
    }


    //inserting id to AlarmBasic
    public long initializeAlarmBasic(int id, int onOff, String type, String path, String title, int status) {
        ContentValues contentValues=new ContentValues();

        contentValues.put(id_ab,id);
        contentValues.put(onOff_ab,onOff);
        contentValues.put(type_ab,type);
        contentValues.put(path_ab,path);
        contentValues.put(title_ab,title);
        contentValues.put(status_ab,status);

        return sqLiteDatabase.insert(DATABASE_TABLE_AlarmBasic,null,contentValues);
    }

    //inserting id to AlarmDays
    public long initializeAlarmDays(int id, int mon, int tue, int wed, int thur, int fri, int sat, int sun, int status) {
        ContentValues contentValues=new ContentValues();

        contentValues.put(id_ad,id);
        contentValues.put(mon_ad,mon);
        contentValues.put(tue_ad,tue);
        contentValues.put(wed_ad,wed);
        contentValues.put(thur_ad,thur);
        contentValues.put(fri_ad,fri);
        contentValues.put(sat_ad,sat);
        contentValues.put(sun_ad,sun);
        contentValues.put(status_ad,status);

        return sqLiteDatabase.insert(DATABASE_TABLE_AlarmDays,null,contentValues);
    }

    //inserting id to AlarmSnooze
    public long initializeAlarmSnooze(int id, int notime, int min, int status) {
        ContentValues contentValues=new ContentValues();

        contentValues.put(id_as,id);
        contentValues.put(duration_as,notime);
        contentValues.put(noTime_as,min);
        contentValues.put(status_as,status);

        return sqLiteDatabase.insert(DATABASE_TABLE_AlarmSnooze,null,contentValues);
    }

    //inserting id to AlarmDate
    public long initializeAlarmDate(int id, int day, int month, int year, int status) {
        ContentValues contentValues=new ContentValues();

        contentValues.put(id_adt,id);
        contentValues.put(day_adt,day);
        contentValues.put(month_adt,month);
        contentValues.put(year_adt,year);
        contentValues.put(status_adt,status);

        return sqLiteDatabase.insert(DATABASE_TABLE_AlramDate,null,contentValues);
    }

    //inserting to AlarmTime
    public long initializeAlarmTime(int id, int hour, int min, int status) {
        ContentValues contentValues=new ContentValues();

        contentValues.put(id_at,id);
        contentValues.put(hour_at,hour);
        contentValues.put(min_at,min);
        contentValues.put(status_at,status);

        return sqLiteDatabase.insert(DATABASE_TABLE_AlarmTime,null,contentValues);
    }

    //get initialize AlarmBasic data
    public Cursor getAlarmBasic_inilialized() {
        return sqLiteDatabase.query(DATABASE_TABLE_AlarmBasic,new String[]{id_ab,onOff_ab,type_ab,path_ab,type_ab,status_ab}
                ,null,null,null,null,null);
    }

    //get initialize AlarmDays data
    public Cursor getAlarmDays_inilialized() {
        return sqLiteDatabase.query(DATABASE_TABLE_AlarmDays,new String[]{id_ad,mon_ad,tue_ad,wed_ad,thur_ad,fri_ad,sat_ad,
                sun_ad,status_ad},null,null,null,null,null);
    }

    //get initialize AlarmSnooze data
    public Cursor getAlarmSnooze_inilialized() {
        return sqLiteDatabase.query(DATABASE_TABLE_AlarmSnooze,new String[]{id_as,noTime_as,duration_as,status_as},
                null,null,null,null,null);
    }

    //get initialize AlarmDate data
    public Cursor getAlarmDate_inilialized() {
        return sqLiteDatabase.query(DATABASE_TABLE_AlramDate,new String[]{id_adt,day_adt,month_adt,year_adt,status_adt},
                null,null,null,null,null);
    }

    //get initialize AlarmTime data
    public Cursor getAlarmTime_inilialized() {
        return sqLiteDatabase.query(DATABASE_TABLE_AlarmTime,new String[]{id_at,hour_at,min_at,status_at},null,null,null,
                null,null);
    }

    public Cursor getData() {
        return sqLiteDatabase.rawQuery(query,null);
    }

    public boolean updateAlarmBasic(int id, int onOff, String type, String path, String title) {
        ContentValues contentValues=new ContentValues();

        contentValues.put(id_ab,id);
        contentValues.put(onOff_ab,onOff);
        contentValues.put(type_ab,type);
        contentValues.put(path_ab,path);
        contentValues.put(title_ab,title);
        contentValues.put(status_ab,1);

        return sqLiteDatabase.update(DATABASE_TABLE_AlarmBasic,contentValues,id_ab + "=" + id,null)>0;
    }

    public boolean updateAlarmDays(int id, int mon, int tue, int wed, int thur, int fri, int sat, int sun) {
        ContentValues contentValues=new ContentValues();

        contentValues.put(id_ad,id);
        contentValues.put(mon_ad,mon);
        contentValues.put(tue_ad,tue);
        contentValues.put(wed_ad,wed);
        contentValues.put(thur_ad,thur);
        contentValues.put(fri_ad,fri);
        contentValues.put(sat_ad,sat);
        contentValues.put(sun_ad,sun);
        contentValues.put(status_ad,1);

        return sqLiteDatabase.update(DATABASE_TABLE_AlarmDays,contentValues,id_ad + "=" + id,null)>0;
    }

    public boolean updateAlarmSnooze(int id, int notime, int min) {
        ContentValues contentValues=new ContentValues();

        contentValues.put(id_as,id);
        contentValues.put(duration_as,notime);
        contentValues.put(noTime_as,min);
        contentValues.put(status_as,1);

        return sqLiteDatabase.update(DATABASE_TABLE_AlarmSnooze,contentValues,id_as + "=" + id,null)>0;
    }

    public boolean updateAlarmDate(int id, int day, int month, int year) {
        ContentValues contentValues=new ContentValues();

        contentValues.put(id_adt,id);
        contentValues.put(day_adt,day);
        contentValues.put(month_adt,month);
        contentValues.put(year_adt,year);
        contentValues.put(status_adt,1);

        return sqLiteDatabase.update(DATABASE_TABLE_AlramDate,contentValues,id_adt + "=" + id,null)>0;
    }

    public boolean updateAlarmTime(int id, int hour, int min) {
        ContentValues contentValues=new ContentValues();

        contentValues.put(id_at,id);
        contentValues.put(hour_at,hour);
        contentValues.put(min_at,min);
        contentValues.put(status_at,1);

        return sqLiteDatabase.update(DATABASE_TABLE_AlarmTime,contentValues,id_at + "=" + id,null)>0;
    }








}
