package com.ugb.programacion;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class adaptadorImagenes extends ArrayAdapter<productos> {
    private Context context;

    public adaptadorImagenes(Context context, ArrayList<productos> datosProductosArrayList) {
        super(context, 0, datosProductosArrayList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inicializar seccion usuario
        seccionUsuario seccionUsuario = new seccionUsuario(context.getApplicationContext());


        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
        }
        try {
            productos misProductos = getItem(position);

            TextView tempVal = itemView.findViewById(R.id.lblNombre);
            tempVal.setText(misProductos.getNombre());

            tempVal = itemView.findViewById(R.id.lblMarca);
            tempVal.setText(misProductos.getMarca());

            tempVal = itemView.findViewById(R.id.lblStock);
            tempVal.setText(misProductos.getStock());

            // Imagen
            CircleImageView circleImageView = itemView.findViewById(R.id.imgProductoListVista);
            Bitmap bitmap = BitmapFactory.decodeFile(misProductos.getImgProducto());
            circleImageView.setImageBitmap(bitmap);

            // Calcular el precio
            double costo = Double.parseDouble(misProductos.getCosto());
            double ganancia = Double.parseDouble(misProductos.getGanancia());
            double precio = calcularPrecio(costo, ganancia);

            // Mostrar el precio en la vista
            tempVal = itemView.findViewById(R.id.lblPrecio);
            tempVal.setText(String.format("$%.2f", precio));

        } catch (Exception e) {
            Toast.makeText(context, "Error al mostrar datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }


    // Función para calcular el precio
    private double calcularPrecio(double costo, double ganancia) {
        return costo * (1 + (ganancia / 100));
    }
}



