package com.example.upnbiblioteca.sqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BdUpnBiblioteca extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "BdUpnBiblioteca.db";
    public static final int DATABASE_VERSION = 1;

    // Nombre de la tabla y columnas
    public static final String TABLE_HISTORIAL = "historial";
    public static final String COLUMN_ID = "idHistorial";
    public static final String COLUMN_PALABRA = "palabra";


    public BdUpnBiblioteca(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_HISTORIAL = "CREATE TABLE " + TABLE_HISTORIAL +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PALABRA + " TEXT" +
                ")";
        db.execSQL(CREATE_TABLE_HISTORIAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se ejecuta si la versión de la base de datos cambia
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORIAL);
        onCreate(db);
    }
    public void insertPalabra(String palabra) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PALABRA, palabra);
        db.insert(TABLE_HISTORIAL, null, values);
        db.close();
    }
}
