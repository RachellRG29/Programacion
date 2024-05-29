package com.ugb.programacion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class adaptadorUsuarios extends BaseAdapter {
    Context context;
    ArrayList<amigos> datosAmigosArrayList;
    amigos misAmigos;
    LayoutInflater layoutInflater;

    public adaptadorUsuarios(Context context, ArrayList<amigos> datosAmigosArrayList) {
        this.context = context;
        this.datosAmigosArrayList = datosAmigosArrayList;
        
    }
    @Override
    public int getCount() {
        return datosAmigosArrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return datosAmigosArrayList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i; //Long.parseLong(datosProductosArrayList.get(i).getIdProducto());
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_user, viewGroup, false);
        try{
            misAmigos = datosAmigosArrayList.get(i);


            TextView tempVal = itemView.findViewById(R.id.lblNombreUser);
            tempVal.setText(misAmigos.getNombreuser());

            //Imagen----
            CircleImageView circleImageView = itemView.findViewById(R.id.imgUserListVista);
            Bitmap bitmap = BitmapFactory.decodeFile(misAmigos.getImgusuario());
            circleImageView.setImageBitmap(bitmap);


        }catch (Exception e){
            Toast.makeText(context, "Error al mostrar datos: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }



}

