package com.ugb.programacion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;

//Agregar usuarios o amigos
public class Usuarios extends AppCompatActivity {
    Button btnGuardarAmigos;
    CircleImageView cirimgAmigo;
    ImageView imgAtrasListaUsuario;
    String accion="nuevo", id="", imgproductourl="", imgproductoFirebaseurrl="", rev="", idProducto="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));

        //valores para los productos
        EditText txtnombre= (EditText)findViewById(R.id.txtnombre);
        EditText txtdireccion= (EditText)findViewById(R.id.txtdireccion);
        EditText txtemail= (EditText)findViewById(R.id.txtemail);
        EditText txtdui= (EditText)findViewById(R.id.txtdui);
        EditText txttelefono= (EditText)findViewById(R.id.txtTelefono);

        //Regresar atras a la lista de usuarios o amigos
        imgAtrasListaUsuario= findViewById(R.id.imgAtras);
        imgAtrasListaUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irListaUsuarios();
            }
        });


        //Cambiar la imagen del usuario con camara o galeria
        cirimgAmigo = findViewById(R.id.imgAmigo);
        cirimgAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(Usuarios.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


    //Guardar los datos de los usuarios o amigos con el boton
        btnGuardarAmigos = findViewById(R.id.btnGuardarAmigos);
        btnGuardarAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtnombre.getText().toString().isEmpty() || txtdireccion.getText().toString().isEmpty() ||
                        txtemail.getText().toString().isEmpty() || txtdui.getText().toString().isEmpty() ||
                        txttelefono.getText().toString().isEmpty()  ){


                    txtnombre.setError("Campo requerido");
                    txtdireccion.setError("Campo requerido");
                    txtemail.setError("Campo requerido");
                    txtdui.setError("Campo requerido");
                    txttelefono.setError("Campo requerido");
                    return;
                } else {
                    //Guardar usuarios con firebase
                    try {
                        //subirFotoFirestore();
                    }catch (Exception e){
                        mostrarMsg("Error al llamar metodos de subir fotos: "+ e.getMessage());
                    }

                }




            }
        });





    }



    //Private voids

    //Camara y galeria totalmente funcional
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    cirimgAmigo.setImageURI(uri);
                    //imgproductourl = uri.getPath(); // Almacenar la URL de la imagen :3
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

    private void irListaUsuarios(){ //ir o regresar a la lista
        Intent abrirVentana = new Intent(getApplicationContext(), lista_usuarios.class);
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

} //fin usuarios
