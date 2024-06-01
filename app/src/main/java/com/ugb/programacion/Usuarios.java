package com.ugb.programacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

//Agregar usuarios o amigos
public class Usuarios extends AppCompatActivity {
    Button btnGuardarAmigos;
    CircleImageView cirimgAmigo;
    ImageView imgAtrasListaUsuario;
    TextView tempVal;
    String accion="nuevo", id="", imgusuariourl="", getimgusuariosFirebaseurl="", rev="", idAmigo="";
    utilidades utls;
    detectarInternet di;
    DatabaseReference databaseReference;

    JSONArray datosUserJSON;
    JSONObject jsonUserObject;
    String miToken="";
    Token objToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));

        obtenerToken();
        di = new detectarInternet(getApplicationContext());
        utls = new utilidades();

        //valores para los productos
        EditText txtnombreuser= (EditText)findViewById(R.id.txtnombreuser);
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

                if(txtnombreuser.getText().toString().isEmpty() || txtdireccion.getText().toString().isEmpty() ||
                        txtemail.getText().toString().isEmpty() || txtdui.getText().toString().isEmpty() ||
                        txttelefono.getText().toString().isEmpty()  ){


                    txtnombreuser.setError("Campo requerido");
                    txtdireccion.setError("Campo requerido");
                    txtemail.setError("Campo requerido");
                    txtdui.setError("Campo requerido");
                    txttelefono.setError("Campo requerido");
                    return;
                } else {
                    //Guardar usuarios con firebase
                    try {
                        subirFotoFirestore();
                    }catch (Exception e){
                        mostrarMsg("Error al llamar metodos de subir fotos: "+ e.getMessage());
                    }

                }

            }
        });

        mostrarDatosAmigos();

    }



    //Private voids

    private void mostrarDatosAmigos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if( accion.equals("modificar") ){
                JSONObject jsonUserObject = new JSONObject(parametros.getString("amigos")).getJSONObject("value");
                id = jsonUserObject.getString("_id");
                rev = jsonUserObject.getString("_rev");
                idAmigo = jsonUserObject.getString("idAmigo");

                tempVal = findViewById(R.id.txtnombreuser);
                tempVal.setText(jsonUserObject.getString("nombre"));

                tempVal = findViewById(R.id.txtdireccion);
                tempVal.setText(jsonUserObject.getString("direccion"));

                tempVal = findViewById(R.id.txtTelefono);
                tempVal.setText(jsonUserObject.getString("telefono"));

                tempVal = findViewById(R.id.txtemail);
                tempVal.setText(jsonUserObject.getString("email"));

                tempVal = findViewById(R.id.txtdui);
                tempVal.setText(jsonUserObject.getString("dui"));

                imgusuariourl = jsonUserObject.getString("imgusuario");
                Bitmap bitmap = BitmapFactory.decodeFile(imgusuariourl);
                cirimgAmigo.setImageBitmap(bitmap);
            }else{ //nuevo registro
                idAmigo = utls.generarIdUnico();
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar los datos: "+ e.getMessage());
        }
    }

    void obtenerToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(tarea-> {
            if (!tarea.isSuccessful()) {
                return;
            }
            miToken = tarea.getResult();
        });
    }
    private void subirFotoFirestore(){
        mostrarMsg("Subiendo foto al servidor...");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        Uri file = Uri.fromFile(new File(imgusuariourl));
        final StorageReference reference = storageReference.child("fotosuser/"+file.getLastPathSegment());

        final UploadTask uploadTask = reference.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mostrarMsg("Error al subir la foto: "+ e.getMessage());
            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mostrarMsg("Foto subida con exito.");
                Task<Uri> downloadUri = uploadTask.continueWithTask(task -> reference.getDownloadUrl()).addOnCompleteListener(task -> {
                    if( task.isSuccessful() ){
                        getimgusuariosFirebaseurl = task.getResult().toString();
                        guardarAmigos();
                    }else{
                        mostrarMsg("La foto se subio, pero con errores, nose pudo obtener el enlace.");
                    }
                });
            }
        });
    }
    private void guardarAmigos(){
        try {
            tempVal = findViewById(R.id.txtnombreuser);
            String nombreuser = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtdireccion);
            String direccion = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtTelefono);
            String telefono = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtemail);
            String email = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtdui);
            String dui = tempVal.getText().toString();

            databaseReference = FirebaseDatabase.getInstance().getReference("amigos");
            String key  = databaseReference.push().getKey();

            if( miToken.equals("") || miToken==null ){
                obtenerToken();
            }
            amigos amigo = new amigos(idAmigo,nombreuser,direccion,telefono,email,dui,imgusuariourl,getimgusuariosFirebaseurl,miToken);
            if( key!=null ){
                databaseReference.child(key).setValue(amigo).addOnSuccessListener(unused -> {
                    mostrarMsg("Usuario registrado con exito.");
                    irListaUsuarios();
                });
            }else{
                mostrarMsg("Error no se inserto el usuario en la base de datos.");
            }
        }catch (Exception ex){
            mostrarMsg("Error al guardar usuario: "+ex.getMessage());
        }
    }

    //Camara y galeria totalmente funcional
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    cirimgAmigo.setImageURI(uri);
                    imgusuariourl = uri.getPath(); // Almacenar la URL de la imagen :3
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
