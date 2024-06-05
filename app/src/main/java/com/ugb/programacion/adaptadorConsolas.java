package com.ugb.programacion;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class adaptadorConsolas extends BaseAdapter {
    Context context;
    ArrayList<productos> datosProductosArrayList;
    LayoutInflater layoutInflater;

    public adaptadorConsolas(Context context, ArrayList<productos> datosProductosArrayList) {
        this.context = context;
        this.datosProductosArrayList = datosProductosArrayList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datosProductosArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return datosProductosArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Inicializar seccion usuario
        seccionUsuario seccionUsuario = new seccionUsuario(context.getApplicationContext());

        View itemView = convertView;
        if (itemView == null) {
            itemView = layoutInflater.inflate(R.layout.listview_consolas, parent, false);
        }

        productos misProductos = datosProductosArrayList.get(position);

        try {
            TextView tempVal = itemView.findViewById(R.id.lblCodigo);
            tempVal.setText(misProductos.getCodigo());

            tempVal = itemView.findViewById(R.id.lblNombre);
            tempVal.setText(misProductos.getNombre());

            tempVal = itemView.findViewById(R.id.lblMarca);
            tempVal.setText(misProductos.getMarca());

            tempVal = itemView.findViewById(R.id.lblCosto);
            tempVal.setText(misProductos.getCosto());

            tempVal = itemView.findViewById(R.id.lblStock);
            tempVal.setText(misProductos.getStock());

            tempVal = itemView.findViewById(R.id.lblGanancia);
            tempVal.setText(misProductos.getGanancia());

            tempVal = itemView.findViewById(R.id.lblDescripcion);
            tempVal.setText(misProductos.getDescripcion());

            //Imagen----
            CircleImageView circleImageView = itemView.findViewById(R.id.imgProductoListConsola);
            Bitmap bitmap = BitmapFactory.decodeFile(misProductos.getImgProducto());
            circleImageView.setImageBitmap(bitmap);

            // Calcular el precio
            double costo = Double.parseDouble(misProductos.getCosto());
            double ganancia = Double.parseDouble(misProductos.getGanancia());
            double precio = calcularPrecio(costo, ganancia);

            // Mostrar el precio en la vista
            tempVal = itemView.findViewById(R.id.lblPrecio);
            tempVal.setText(String.format("$%.2f", precio));

            // Botón regresar
            Button btnRegresarLista = itemView.findViewById(R.id.btn_regresarlistimg);
            btnRegresarLista.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, lista_delivery.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

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

