package com.ugb.programacion;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB_register extends SQLiteOpenHelper{
    private  static  final String dbname = "db_register";
    private  static final int v=1;

    private static  final String SQldb = "CREATE TABLE register(idProducto text, nombre text, correo text, contrasena text)";

    public DB_register(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, v);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQldb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //para actualizar la base de datos
    }



} //fin db register


