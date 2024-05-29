package com.ugb.programacion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB_user extends SQLiteOpenHelper {
    private  static  final String dbname = "db_user";
    private  static final int v=1;
    private static  final String SQldb = "CREATE TABLE amigos(id text, rev text, idAmigo text, nombreuser text, direccion text, telefono text, email text, dui text, imgusuario text, imgusuariosFirebaseurl text, token text)";

    public DB_user(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
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

    //ADMINISTRAR LOS PRODUCTOS CONSOLES EXPRESS
    public String administrar_amigos(String accion, String[] datos){
        try {
            SQLiteDatabase db = getWritableDatabase();
            if (accion.equals("nuevo")){
                db.execSQL("INSERT INTO amigos(id,rev,idAmigo,nombreuser,direccion,telefono,email,dui,imgusuario,actualizado) VALUES('"+ datos[0] +"', '"+ datos[1] +"', '"+
                        datos[2] +"', '"+ datos[3] +"','"+ datos[4] +"','"+ datos[5] +"','"+ datos[6] +"','"+ datos[7] +"', '"+ datos[8] +"','"+ datos[9] +"' )");
            } else if (accion.equals("modificar")) {
                db.execSQL("UPDATE amigos SET id='"+ datos[0] +"',rev='"+ datos[1] +"',nombreuser='"+ datos[3] +"',direccion='"+ datos[4] +"',telefono='"+ datos[5] +"',email='"+ datos[6] +
                        "',dui='"+ datos[7] +"',imgusuario='"+ datos[8] +"',actualizado='"+ datos[9] +   "' WHERE idAmigo='"+ datos[2] +"'");
            } else if (accion.equals("eliminar")) {
                db.execSQL("DELETE FROM amigos WHERE idProducto='"+ datos[2] +"'");
            }
            return "ok";
        } catch (Exception e){
            return  "Error: "+ e.getMessage();

        }
    }

    //CURSOS CONSULTAR PRODUCTOS
    public Cursor consultar_amigos(){
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursoruser = db.rawQuery("SELECT * FROM amigos ORDER BY nombreuser", null);
        return cursoruser;
    }

    public Cursor pendienteSincronizar(){
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from amigos where actualizado='no'", null);
        return cursor;
    }

}
