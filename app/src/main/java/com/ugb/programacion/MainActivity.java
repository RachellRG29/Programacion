package com.ugb.programacion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

//AGREGAR PRODUCTOS TIENDA ONLINE PRODUCTOS PARCIAL II

public class MainActivity extends AppCompatActivity {

    //VARIABLES

    //Navegation bar
    BottomNavigationView bottomNavigationView;
    CircleImageView imageCirProducto;
    FloatingActionButton btnChangeImage;
    String accion="nuevo", id="", imgproductourl="", rev="", idProducto="";
    Button btnGuardar;
    TextView tempVal;
    utilidades utls;
    detectarInternet di;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICKER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.blueblue));
        di = new detectarInternet(getApplicationContext());
        utls = new utilidades();

        //valores para los productos
        EditText txtcodigo= (EditText)findViewById(R.id.txtCodigo);
        EditText txtnombre= (EditText)findViewById(R.id.txtNombre);
        EditText txtmarca= (EditText)findViewById(R.id.txtMarca);
        EditText txtcosto= (EditText)findViewById(R.id.txtCosto);
        EditText txtstock= (EditText)findViewById(R.id.txtStock);
        EditText txtganancia= (EditText)findViewById(R.id.txtGanancia);
        EditText txtdescripcion= (EditText)findViewById(R.id.txtDescripcion);

        imageCirProducto = findViewById(R.id.imgProductoVista); //
        btnChangeImage = findViewById(R.id.btnCambiarImagen);
        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               ImagePicker.with(MainActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });

        //BottomNavegation
        bottomNavigationView = findViewById(R.id.bottomNavegation);
        bottomNavigationView.setSelectedItemId(R.id.navAgregar);

        bottomNavigationView.setOnItemReselectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navPrincipal: //Lista tulip
                    startActivity(new Intent(getApplicationContext(), lista_delivery.class));
                    finish();
                    return;
                case R.id.navAgregar: //Agregar tulip
                    return;
                case R.id.navMensajeria: //Mensajeria
                    startActivity(new Intent(getApplicationContext(), Mensajeria.class));
                    finish();
                    return;
            }
            return;
        }); //fin navegation



        //boton guardar producto
        btnGuardar = findViewById(R.id.btnGuardarProducto);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //validar campo obligatorio
                if(txtcodigo.getText().toString().isEmpty() || txtnombre.getText().toString().isEmpty() ||
                        txtmarca.getText().toString().isEmpty() || txtcosto.getText().toString().isEmpty() ||
                        txtstock.getText().toString().isEmpty() || txtganancia.getText().toString().isEmpty() ||
                        txtdescripcion.getText().toString().isEmpty() ){

                    txtcodigo.setError("Campo requerido");
                    txtnombre.setError("Campo requerido");
                    txtmarca.setError("Campo requerido");
                    txtcosto.setError("Campo requerido");
                    txtstock.setError("Campo requerido");
                    txtganancia.setError("Campo requerido");
                    txtdescripcion.setError("Campo requerido");
                    return;
                } else {

                    try {
                        //VALIDAR AGREGAR PRODUCTO
                        tempVal = findViewById(R.id.txtCodigo);
                        String codigo = tempVal.getText().toString();

                        tempVal = findViewById(R.id.txtNombre);
                        String nombre = tempVal.getText().toString();

                        tempVal = findViewById(R.id.txtMarca);
                        String marca = tempVal.getText().toString();

                        tempVal = findViewById(R.id.txtCosto);
                        String costo = tempVal.getText().toString();

                        tempVal = findViewById(R.id.txtStock);
                        String stock = tempVal.getText().toString();

                        tempVal = findViewById(R.id.txtGanancia);
                        String ganancia = tempVal.getText().toString();

                        tempVal = findViewById(R.id.txtDescripcion);
                        String descripcion = tempVal.getText().toString();

                        String respuesta = "", actualizado= "no";

                        //Si hay internet se guarde en el servidor
                        if(di.hayConexionInternet()){
                            //GUARDAR DATOS SERVIDOR
                            JSONObject datosProductos = new JSONObject();
                            if (accion.equals("modificar") && id.length() > 0 && rev.length() > 0) {
                                datosProductos.put("_id", id);
                                datosProductos.put("_rev", rev);
                            }
                            datosProductos.put("idProducto", idProducto);
                            datosProductos.put("codigo", codigo);
                            datosProductos.put("nombre", nombre);
                            datosProductos.put("marca", marca);
                            datosProductos.put("costo", costo);
                            datosProductos.put("stock", stock);
                            datosProductos.put("ganancia", ganancia);
                            datosProductos.put("descripcion", descripcion);
                            datosProductos.put("imgproducto", imgproductourl);
                            datosProductos.put("actualizado", "si");


                            enviarDatosServidor objGuardarDatosServidor = new enviarDatosServidor(getApplicationContext());
                            respuesta = objGuardarDatosServidor.execute(datosProductos.toString()).get();

                            JSONObject respuestaJSONObject = new JSONObject(respuesta);
                            if (respuestaJSONObject.getBoolean("ok")) {
                                id = respuestaJSONObject.getString("id");
                                rev = respuestaJSONObject.getString("rev");
                                actualizado ="si";
                            } else {
                                respuesta = "Error al guardar en servidor: " + respuesta;
                            }

                        }

                        DB db = new DB(getApplicationContext(), "", null, 1);
                        String[] datos = new String[]{id, rev, idProducto, codigo, nombre, marca, costo, stock, ganancia, descripcion, imgproductourl, actualizado};
                        respuesta = db.administrar_productos(accion, datos);
                        if (respuesta.equals("ok")) {
                            Toast.makeText(getApplicationContext(), "Producto Registrado con Exito.", Toast.LENGTH_SHORT).show();
                            irVista();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + respuesta, Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        mostrarMsg("Error: " + e.getMessage());
                    }

                } //validar campos obligatorios :3
            }
        });
        mostrarDatosProductos(); //mostrar los datos del producto




    } //fin ONCREATE :3

    //AGREGAR PRIVATE VOIDS

    //Camara y galeria totalmente funcional
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    imageCirProducto.setImageURI(uri);
                    imgproductourl = uri.getPath(); // Almacenar la URL de la imagen :3
                } else {
                    mostrarMsg("No se pudo obtener la imagen seleccionada.");
                }
            } else {
                mostrarMsg("Se canceló la selección de la imagen.");
            }
        } catch (Exception e) {
            mostrarMsg("Error al mostrar la imagen: " + e.getMessage());
        }
    }




    //MOSTRAR DATOS PRODUCTOS DELIVERY CONSOLAS
    private void mostrarDatosProductos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if( accion.equals("modificar") ){
                JSONObject jsonObject = new JSONObject(parametros.getString("productos")).getJSONObject("value");
                id = jsonObject.getString("_id");
                rev = jsonObject.getString("_rev");
                idProducto = jsonObject.getString("idProducto");

               // tempVal = findViewById(R.id.txtCodigo);
                tempVal.setText(jsonObject.getString("codigo"));

               // tempVal = findViewById(R.id.txtNombre);
                tempVal.setText(jsonObject.getString("nombre"));

               // tempVal = findViewById(R.id.txtMarca);
                tempVal.setText(jsonObject.getString("marca"));

               // tempVal = findViewById(R.id.txtCosto);
                tempVal.setText(jsonObject.getString("costo"));

               // tempVal = findViewById(R.id.txtStock);
                tempVal.setText(jsonObject.getString("stock"));

              //  tempVal = findViewById(R.id.txtGanancia);
                tempVal.setText(jsonObject.getString("ganancia"));

               // tempVal = findViewById(R.id.txtDescripcion);
                tempVal.setText(jsonObject.getString("descripcion"));

                imgproductourl = jsonObject.getString("imgproducto");
                Bitmap bitmap = BitmapFactory.decodeFile(imgproductourl);
                imageCirProducto.setImageBitmap(bitmap);
            }else{ //nuevo registro
                idProducto = utls.generarIdUnico();
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar los datos: "+ e.getMessage());
        }
    }

    //Boton navegation
    private void irVista(){ //ir o regresar a la lista
        Intent abrirVentana = new Intent(getApplicationContext(), lista_delivery.class);
        startActivity(abrirVentana);
    }

    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    //CAMBIAR EL COLOR DE LA BARRA DE ESTADO
    private void cambiarColorBarraEstado(int color) {
        // Verificar si la versión del SDK es Lollipop o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    } //fin cambiar colorbarraestado

} // fin MainActivity agregar productos