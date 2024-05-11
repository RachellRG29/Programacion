package com.ugb.programacion;
//cindy
import java.util.Base64;
public class utilidades {
    //ipconfig para revisar la direccion de la maquina, puede cambiar
    static String urlConsulta = "http://192.168.1.2:5984/yaritza/_design/cindy/_view/cindy";
    static String urlMto = "http://192.168.1.2:5984/yaritza";
    static String user = "Cindy";
    static String passwd = "couch129.29";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user +":"+ passwd).getBytes());
    public String generarIdUnico(){
        return java.util.UUID.randomUUID().toString();
    }
}

/*
  "_id": "2feca9ea7aed3d28830f7e20c00502c3",
  "_rev": "1-91b35a5fcee8238d634090e5537fabb4",
  "idProducto": "",
  "codigo": "MK-32",
  "nombre": "Peluche",
  "marca": "Peluchiis",
  "costo": "34",
  "stock": "2",
  "ganancia": "6",
  "descripcion": "lorem imsup",
  "imgproducto": ""





  <ListView
                android:id="@+id/ltsProductos"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.0"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

 */