package com.ugb.programacion;

import android.content.Context;
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




/*
public class adaptadorImagenes extends ArrayAdapter<productos> {
    private Context context;

    public adaptadorImagenes(Context context, ArrayList<productos> datosProductosArrayList) {
        super(context, 0, datosProductosArrayList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

            //Imagen----
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
*/

/*
package com.ugb.programacion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class adaptadorImagenes extends BaseAdapter {
    Context context;
    ArrayList<productos> datosProductosArrayList;
    productos misProductos;
    LayoutInflater layoutInflater;

    public adaptadorImagenes(Context context, ArrayList<productos> datosProductosArrayList) {
        this.context = context;
        this.datosProductosArrayList = datosProductosArrayList;

    }
    @Override
    public int getCount() {
        return datosProductosArrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return datosProductosArrayList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

   @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_imagenes, viewGroup, false);
        try{
            misProductos = datosProductosArrayList.get(i);

            TextView tempVal = itemView.findViewById(R.id.lblNombre);
            tempVal.setText(misProductos.getNombre());

            tempVal = itemView.findViewById(R.id.lblMarca);
            tempVal.setText(misProductos.getMarca());

            tempVal= itemView.findViewById(R.id.lblStock);
            tempVal.setText(misProductos.getStock());

            //Imagen----
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


        }catch (Exception e){
            Toast.makeText(context, "Error al mostrar datos: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }

    // Función para calcular el precio
    private double calcularPrecio(double costo, double ganancia) {
        return costo * (1 + (ganancia / 100));
    }

}
 */

