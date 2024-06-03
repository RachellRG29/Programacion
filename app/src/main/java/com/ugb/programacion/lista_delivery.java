package com.ugb.programacion;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class lista_delivery extends AppCompatActivity {

    //VARIABLES
    BottomNavigationView bottomNavigationView;
    FloatingActionButton btnIrRegistrar;
    seccionUsuario session;
    TextView lblusuariotexto;
    ImageSlider imageConsoles;
    Bundle parametros = new Bundle();
    ListView lts;
    Cursor cProductos;
    DB dbProductos;
    productos misProductos;
    final ArrayList<productos> alProductos=new ArrayList<productos>();
    final ArrayList<productos> alProductosCopy=new ArrayList<productos>();
    JSONArray datosJSON;
    JSONObject jsonObject;
    obtenerDatosServidor datosServidor;
    detectarInternet di;
    int posicion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_delivery);

        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));

        dbProductos = new DB(lista_delivery.this,"",null,1);

        imageConsoles= findViewById(R.id.imgSlideConsolas); //imagenes desplazables
        setupImageSlider();

        //lblusername, usuario logeado
        lblusuariotexto= findViewById(R.id.lblUsuarioLogeado);

        // Inicializar session del usuario logeado
        session = new seccionUsuario(getApplicationContext());

        // Seccion con los datos registrados
        String usuarioLogeado = getIntent().getStringExtra("usuario_logeado");
        boolean isAdmin = getIntent().getBooleanExtra("is_admin", false);

        if (usuarioLogeado != null) {
            session.createLoginSession(usuarioLogeado, isAdmin);
            lblusuariotexto.setText(usuarioLogeado);
        }


        //Boton agregar
        btnIrRegistrar = findViewById(R.id.btnAgregarProducto);

        // Obtiene el usuario y si es admin el boton se hara visible, de lo contrario no :3
        if (session.isAdmin()) {
            btnIrRegistrar.setVisibility(View.VISIBLE);
        } else {
            btnIrRegistrar.setVisibility(View.GONE);
        }
        btnIrRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle parametros = new Bundle();
                parametros.putString("accion", "nuevo");
                irAgregar(parametros);
            }
        });

        //BottomNavegation
        bottomNavigationView = findViewById(R.id.bottomNavegation);
        bottomNavigationView.setSelectedItemId(R.id.navPrincipal);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navPrincipal:
                        //Lista ConsoleXpress
                        return true;

                    case R.id.navGps:
                        startActivity(new Intent(getApplicationContext(), Gps.class));
                        finish();
                        return true;

                    case R.id.navMensajeria:
                        startActivity(new Intent(getApplicationContext(), lista_usuarios.class));
                        finish();
                        return true;
                }
                return false;
            }
        }); // fin navigation

        try{
            di = new detectarInternet(getApplicationContext());
            if(di.hayConexionInternet()){ //online
                obtenerDatosProductosServidor();
                sincronizar();
            }else{
                obtenerProductos();//offline
            }
        }catch (Exception e){
            mostrarMsg("Error al detectar si hay conexion "+ e.getMessage());
        }
        buscarProductos();

        lts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el producto seleccionado
                productos productoSeleccionado = alProductos.get(position);

                // Inicializar el adaptador adaptadorConsolas con el producto seleccionado
                adaptadorConsolas adConsolas = new adaptadorConsolas(getApplicationContext(), new ArrayList<>(Collections.singletonList(productoSeleccionado)));
                lts.setAdapter(adConsolas);
            }
        });


    } //fin ONCREATE

    //AGREGAR PRIVATE VOIDS

    @Override
    protected void onResume() {
        super.onResume();
        String usuarioLogeado = session.getUsername();
        if (usuarioLogeado != null) {
            lblusuariotexto.setText(usuarioLogeado);
        }
    }

    //Imagenes desplazables
    private void setupImageSlider() {
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.imgconsolakirby, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.imgconsolanintendo, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.imgconsolatulipan, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.imgconsolazeldaespecial, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.imgnintendosupermariobros, ScaleTypes.CENTER_CROP));
        imageConsoles.setImageList(slideModels, ScaleTypes.CENTER_CROP);
    }

    private void sincronizar(){
        try{
            cProductos = dbProductos.pendienteSincronizar();
            if(cProductos.moveToFirst()){ //hay registros pendientes de sincronizar
                jsonObject = new JSONObject();
                mostrarMsg("Sincronizando...");
                do{
                    if(cProductos.getString(0).length()>0 && cProductos.getString(1).length()>0){
                        jsonObject.put("_id", cProductos.getString(0));
                        jsonObject.put("_rev", cProductos.getString(1));

                    }
                    jsonObject.put("idProducto", cProductos.getString(2));
                    jsonObject.put("codigo", cProductos.getString(3));
                    jsonObject.put("nombre", cProductos.getString(4));
                    jsonObject.put("marca", cProductos.getString(5));
                    jsonObject.put("costo", cProductos.getString(6));
                    jsonObject.put("stock", cProductos.getString(7));
                    jsonObject.put("ganancia", cProductos.getString(8));
                    jsonObject.put("descripcion", cProductos.getString(9));
                    jsonObject.put("imgproducto", cProductos.getString(10));
                    jsonObject.put("actualizado", "si");

                    enviarDatosServidor objGuardarDatosServidor = new enviarDatosServidor(getApplicationContext());
                    String respuesta = objGuardarDatosServidor.execute(jsonObject.toString()).get();

                    JSONObject respuestaJSONObject = new JSONObject(respuesta);
                    if (respuestaJSONObject.getBoolean("ok")) {
                        DB db = new DB(getApplicationContext(), "", null, 1);
                        String[] datos = new String[]{
                                respuestaJSONObject.getString("id"),
                                respuestaJSONObject.getString("rev"),
                                jsonObject.getString("idProducto"),
                                jsonObject.getString("codigo"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("marca"),
                                jsonObject.getString("costo"),
                                jsonObject.getString("stock"),
                                jsonObject.getString("ganancia"),
                                jsonObject.getString("descripcion"),
                                jsonObject.getString("imgproducto"),
                                jsonObject.getString("actualizado")
                        };
                        respuesta = db.administrar_productos("modificar", datos);
                        if (!respuesta.equals("ok")) {
                            mostrarMsg(respuesta);
                        }
                    } else{
                        mostrarMsg("Error al sincronizar en servidor: " + respuesta);
                    }
                }while (cProductos.moveToNext());
            }
        }catch (Exception e){
            mostrarMsg("Error al sincronizar: "+e.getMessage());
        }
    }

    private void obtenerDatosProductosServidor(){
        try{
            datosServidor = new obtenerDatosServidor();
            String data = datosServidor.execute().get();
            jsonObject = new JSONObject(data);
            datosJSON = jsonObject.getJSONArray("rows");
            mostrarDatosProductos();
        }catch (Exception e){
            mostrarMsg("Error al obtener datos desde el servidor: "+ e.getMessage());
        }
    }

    private void mostrarDatosProductos(){
        try{
            if( datosJSON.length()>0 ){
                lts = findViewById(R.id.ltsProductos);

                alProductos.clear();
                alProductosCopy.clear();

                JSONObject misDatosJSONObject;
                for (int i=0; i<datosJSON.length(); i++){
                    misDatosJSONObject = datosJSON.getJSONObject(i).getJSONObject("value");
                    misProductos = new productos(
                            misDatosJSONObject.getString("_id"),
                            misDatosJSONObject.getString("_rev"),
                            misDatosJSONObject.getString("idProducto"),
                            misDatosJSONObject.getString("codigo"),
                            misDatosJSONObject.getString("nombre"),
                            misDatosJSONObject.getString("marca"),
                            misDatosJSONObject.getString("costo"),
                            misDatosJSONObject.getString("stock"),
                            misDatosJSONObject.getString("ganancia"),
                            misDatosJSONObject.getString("descripcion"),
                            misDatosJSONObject.getString("imgproducto")
                    );
                    alProductos.add(misProductos);
                }
                adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alProductos);
                lts.setAdapter(adImagenes);
                alProductosCopy.addAll(alProductos);

                registerForContextMenu(lts);
            }else{
                mostrarMsg("No hay datos que mostrar.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar los datos: "+e.getMessage());
        }
    }

    //MENU
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);
        boolean isAdmin = getIntent().getBooleanExtra("is_admin", false);

        if (!isAdmin) {
            menu.findItem(R.id.mnxAgregar).setVisible(false);
            menu.findItem(R.id.mnxModificar).setVisible(false);
            menu.findItem(R.id.mnxEliminar).setVisible(false);
        }

        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            menu.setHeaderTitle(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("nombre"));
        }catch (Exception e){
            mostrarMsg("Error al mostrar el menu: "+ e.getMessage());
        }
    }

    //Agregar, modificar y eliminar
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        boolean isAdmin = getIntent().getBooleanExtra("is_admin", false);

        if (!isAdmin) {
            mostrarMsg("No tienes permiso para realizar esta acción.");
            return super.onContextItemSelected(item);
        }

        try {
            switch (item.getItemId()) {
                case R.id.mnxAgregar:
                    parametros.putString("accion", "nuevo");
                    irAgregar(parametros);
                    break;
                case R.id.mnxModificar:
                    parametros.putString("accion", "modificar");
                    parametros.putString("productos", datosJSON.getJSONObject(posicion).toString());
                    irAgregar(parametros);
                    break;
                case R.id.mnxEliminar:
                    eliminarProductos();
                    break;
            }
            return true;
        } catch (Exception e) {
            mostrarMsg("Error en menu: " + e.getMessage());
            return super.onContextItemSelected(item);
        }
    }


    //Eliminar productos
    private void eliminarProductos(){
        try {
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(lista_delivery.this);
            confirmacion.setTitle("Esta seguro de Eliminar: ");
            confirmacion.setMessage(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("nombre"));
            confirmacion.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        String respuesta = dbProductos.administrar_productos("eliminar", new String[]{"", "", datosJSON.getJSONObject(posicion).getJSONObject("value").getString("idProducto")});
                        if (respuesta.equals("ok")) {
                            mostrarMsg("Consola eliminada con exito.");
                            obtenerProductos();
                        } else {
                            mostrarMsg("Error al eliminar la consola: " + respuesta);
                        }
                    }catch (Exception e){
                        mostrarMsg("Error al eliminar Datos: "+e.getMessage());
                    }
                }
            });
            confirmacion.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            confirmacion.create().show();
        }catch (Exception e){
            mostrarMsg("Error al eliminar: "+ e.getMessage());
        }
    }

    //obtenet productos consoles express
    private void obtenerProductos(){
        try{
            dbProductos = new DB(lista_delivery.this, "", null, 1);
            cProductos = dbProductos.consultar_productos();
            if ( cProductos.moveToFirst() ){
                datosJSON = new JSONArray();
                do{
                    jsonObject = new JSONObject();
                    JSONObject jsonObjectValue = new JSONObject();
                    jsonObject.put("_id",cProductos.getString(0));
                    jsonObject.put("_rev",cProductos.getString(1));
                    jsonObject.put("idProducto",cProductos.getString(2));
                    jsonObject.put("codigo",cProductos.getString(3));
                    jsonObject.put("nombre",cProductos.getString(4));
                    jsonObject.put("marca",cProductos.getString(5));
                    jsonObject.put("costo",cProductos.getString(6));
                    jsonObject.put("stock",cProductos.getString(7));
                    jsonObject.put("ganancia",cProductos.getString(8));
                    jsonObject.put("descripcion",cProductos.getString(9));
                    jsonObject.put("imgproducto",cProductos.getString(10));

                    jsonObjectValue.put("value", jsonObject);
                    datosJSON.put(jsonObjectValue);
                    //alProductos.add(misProductos);
                }while(cProductos.moveToNext());
                mostrarDatosProductos();
            }else{
                mostrarMsg("No hay consolas que mostrar");
            }
        }catch (Exception e){
            mostrarMsg("Error al obtener consolas: "+ e.getMessage());
        }
    }

    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    //Buscar productos consolas
    private void buscarProductos(){

        TextView tempVal;
        tempVal = findViewById(R.id.txt_busqueda);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    alProductos.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if( valor.length()<=0 ){
                        alProductos.addAll(alProductosCopy);
                    }else{
                        for( productos producto : alProductosCopy ){

                            String codigo = producto.getCodigo();
                            String nombre = producto.getNombre();
                            String marca = producto.getMarca();
                            String costo = producto.getCosto();
                            String stock = producto.getStock();
                            String ganancia = producto.getGanancia();
                            String descripcion = producto.getDescripcion();

                            if( codigo.toLowerCase().trim().contains(valor) ||
                                    nombre.toLowerCase().trim().contains(valor) ||
                                    marca.trim().contains(valor) ||
                                    costo.trim().toLowerCase().contains(valor) ||
                                    stock.trim().toLowerCase().contains(valor) ||
                                    ganancia.trim().toLowerCase().contains(valor) ||
                                    descripcion.trim().toLowerCase().contains(valor) ){
                                alProductos.add(producto);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alProductos);
                        lts.setAdapter(adImagenes);
                    }
                }catch (Exception e){
                    mostrarMsg("Error al buscar: "+e.getMessage() );
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void irAgregar(Bundle parametros){
        Intent abrirVentana = new Intent(getApplicationContext(), MainActivity.class);
        abrirVentana.putExtras(parametros); //abrir actividad y modificar
        startActivity(abrirVentana);

    }


    //Metodo para cambiar el color de la barra de estado
    private void cambiarColorBarraEstado(int color) {
        // Verificar si la versión del SDK es Lollipop o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    } //fin cambiar colorbarraestado


} //fin lista_productos


