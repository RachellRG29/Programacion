package com.ugb.programacion;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB_register extends SQLiteOpenHelper{
    private  static  final String dbname = "dbregister";
    private  static final int v=1;
    private static  final String SQldb = "CREATE TABLE register(idRegister text, usuario text, correo text, contrasena text)";

    public DB_register(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db_register) {
        db_register.execSQL(SQldb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db_register, int i, int i1) {
        //para actualizar la base de datos
    }

    //ADMINISTRAR LOS USUARIOS PARA REGISTER
    public String administrar_register(String accion, String[] datos){
        try {
            SQLiteDatabase db_register = getWritableDatabase();
            if (accion.equals("nuevo")){
                db_register.execSQL("INSERT INTO register(idRegister,usuario,correo,contrasena) VALUES('"+ datos[0] +"', '"+ datos[1] +"', '"+ datos[2] +"', '"+ datos[3] +"' )");
            } else if (accion.equals("editar")) {
                db_register.execSQL("UPDATE register SET usuario='"+ datos[1] +"',correo='"+ datos[2] +"',contrasena='"+ datos[3] + "'  WHERE idRegister='"+ datos[0] +"'");
            } else if (accion.equals("eliminar")) {
                db_register.execSQL("DELETE FROM register WHERE idRegister='"+ datos[2] +"'");
            }
            return "ok";
        } catch (Exception e){
            return  "Error: "+ e.getMessage();

        }
    }

    //CURSOS CONSULTAR REGISTROS
   /* public Cursor consultar_registros(){
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM register ORDER BY usuario", null);
        return cursor;
    }
    */


} //fin db register





