package com.mikekmangum.fishingholesmain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "catchDatabase.db";
    private static DatabaseHelper sInstance;


    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE " + DbSchema.CatchTable.NAME + "(" +
                DbSchema.CatchTable.Cols.CATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbSchema.CatchTable.Cols.TIME_OF_DAY + " TEXT, " +
                DbSchema.CatchTable.Cols.FISH_SPECIES + " TEXT, " +
                DbSchema.CatchTable.Cols.LENGTH + " REAL, " +
                DbSchema.CatchTable.Cols.WEIGHT + " REAL, " +
                DbSchema.CatchTable.Cols.LURE + " TEXT, " +
                DbSchema.CatchTable.Cols.LATITUDE + " REAL, " +
                DbSchema.CatchTable.Cols.LONGITUDE + " REAL, " +
                DbSchema.CatchTable.Cols.TEMPERATURE + " REAL, " +
                DbSchema.CatchTable.Cols.CONDITIONS + " TEXT, " +
                DbSchema.CatchTable.Cols.PICTURE + " BLOB)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbSchema.CatchTable.NAME) ;
        onCreate(db);
    }

    public void addCatch(Catch c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(c);
        db.insert(DbSchema.CatchTable.NAME , null,values);
    }

    public Cursor getCatch(int row)  {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DbSchema.CatchTable.NAME +
                " WHERE " + DbSchema.CatchTable.Cols.CATCH_ID + " = "  + row, null);
        return res;
    }

    private static ContentValues getContentValues(Catch c){
        ContentValues values = new ContentValues();
        values.put(DbSchema.CatchTable.Cols.TIME_OF_DAY, c.getTimestamp().toString());
        values.put(DbSchema.CatchTable.Cols.FISH_SPECIES, c.getSpecies());
        values.put(DbSchema.CatchTable.Cols.LENGTH, c.getLength());
        values.put(DbSchema.CatchTable.Cols.WEIGHT, c.getWeight());
        values.put(DbSchema.CatchTable.Cols.LURE, c.getLure());
        values.put(DbSchema.CatchTable.Cols.LATITUDE, c.getLatitude());
        values.put(DbSchema.CatchTable.Cols.LONGITUDE, c.getLongitude());
        values.put(DbSchema.CatchTable.Cols.TEMPERATURE, c.getTemperature());
        values.put(DbSchema.CatchTable.Cols.CONDITIONS, c.getConditions());
        values.put(DbSchema.CatchTable.Cols.PICTURE, c.getPicture());

        return values;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + DbSchema.CatchTable.NAME,null);
        return res;
    }

}
