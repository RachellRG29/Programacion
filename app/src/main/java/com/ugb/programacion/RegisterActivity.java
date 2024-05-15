package com.ugb.programacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//firebase
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    //FirebaseFirestore mFirestore;
   // FirebaseAuth mAuth;
    TextView tempReg;
    String accion="nuevo", idRegister="";
    int posicion=0;
    Bundle parametros = new Bundle();

    private EditText usuario, correo, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.red));
        idRegister = generarIdUnico();


       // mFirestore = FirebaseFirestore.getInstance();
        //mAuth = FirebaseAuth.getInstance();

        //txt lbl_signup_aActivitySignup a Registrar en caso de no tener cuenta
        TextView lblIngresar = (TextView) findViewById(R.id.lbl_register_aActivityLogin);
        lblIngresar.setOnClickListener(v -> openRegistrarlbl());

        //Valores usuario, contraseña y email
        usuario = (EditText)findViewById(R.id.txtUsuarioRegister);
        correo = (EditText)findViewById(R.id.txtCorreoRegister);
        contrasena = (EditText)findViewById(R.id.txtContrasenaRegister);



        //Boton para registrar
        Button btn_register = findViewById(R.id.btn_registrar);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameUser = usuario.getText().toString().trim();
                String emailUser = correo.getText().toString().trim();
                String passUser = contrasena.getText().toString().trim();

                if (usuario.getText().toString().isEmpty() || nameUser.isEmpty()
                        || correo.getText().toString().isEmpty() ||emailUser.isEmpty()
                        || contrasena.getText().toString().isEmpty()  || passUser.isEmpty()   ) {
                    usuario.setError("Campo requerido");
                    correo.setError("Campo requerido");
                    contrasena.setError("Campo requerido");
                    return;

                } else {
                    //Validar registros
                    try {

                        tempReg = findViewById(R.id.txtUsuarioRegister);
                        String usuario = tempReg.getText().toString();

                        tempReg = findViewById(R.id.txtCorreoRegister);
                        String correo = tempReg.getText().toString();

                        tempReg = findViewById(R.id.txtContrasenaRegister);
                        String contrasena = tempReg.getText().toString();

                        String respuesta = "";

                        //Guardar local
                        DB_register db_register = new DB_register(getApplicationContext(), "", null, 1);
                        String[] datos = new String[]{idRegister, usuario, correo, contrasena};
                        respuesta = db_register.administrar_register(accion, datos);
                        if (respuesta.equals("ok")) {
                            mostrarMsg("Usuario Registrado con Exito ");
                            irLogin(); //ir login si se registro
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + respuesta, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e){

                    }

                   // registerUser(nameUser, emailUser, passUser); //firebase
                }
            }
        });


    } //onCreate

 //firebase
  /*  private void registerUser(String nameUser, String emailUser, String passUser) {
        mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                try {
                    String id = mAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id",id);
                    map.put( "name", nameUser);
                    map.put( "email", emailUser);
                    map.put( "password", passUser);

                    mFirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            try {
                                //finish();
                                startActivity(new Intent(RegisterActivity.this, Login.class));
                                mostrarMsg("Usuario registrado con exitor ");

                            } catch (Exception e){
                                //Toast.makeText(RegisterActivity.this, "Usuario registrado con exito", Toast.LENGTH_SHORT).show();
                                mostrarMsg("Error al registrar: "+ e.getMessage());
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                            mostrarMsg("Error al obtener productos: "+ e.getMessage());
                        }
                    });


                }catch (Exception e){
                    mostrarMsg("Error en registro: "+ e.getMessage());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                mostrarMsg("Error al registrar: "+ e.getMessage());
            }
        });

    }
    */

    //Private void

    //MENU
    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_register, menu);

        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
           // menu.setHeaderTitle(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("nombre"));
        }catch (Exception e){
            mostrarMsg("Error al mostrar el menu: "+ e.getMessage());
        }
    }

    //Agregar, editar y eliminar

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            switch (item.getItemId()){
                case R.id.opcAgregar:
                    parametros.putString("accion", "nuevo");
                    //irAgregar(parametros);
                    break;
                case R.id.mnxModificar:
                    parametros.putString("accion","editar");
                   // parametros.putString("register", datosJSON.getJSONObject(posicion).toString());
                   // irAgregar(parametros);
                    break;
                case R.id.mnxEliminar:
                    eliminarRegistros();
                    break;
            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error en menu: "+ e.getMessage());
            return super.onContextItemSelected(item);
        }

    }

     */

    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public String generarIdUnico(){
        return java.util.UUID.randomUUID().toString();
    }

    /*public void eliminarRegistros (){
        DB_register db_register= new DB_register(this,"register",null,1);
        SQLiteDatabase sql_register=db_register.getWritableDatabase();
        //String cedula=etCedula.getText().toString();
        int cant = sql_register.delete("register","idRegister='"+idRegister+"'",null);
        sql_register.close();
        if(cant==1){
            Toast.makeText(this,"Se borro el Registro",Toast.LENGTH_LONG).show();
            //idRegister.setText("");
            idRegister.toString();
            usuario.setText("");
            correo.setText("");
            contrasena.setText("");

        }else{
            Toast.makeText(this,"No existe un usuario con ese ID",Toast.LENGTH_LONG).show();
        }
    }
    */

    //ya habido registrado, ir a la pantalla principal de la lista delivery
    private void irLogin(){ //
        Intent abrirVentana = new Intent(getApplicationContext(), Login.class);
        startActivity(abrirVentana);
    }


    //abrir view main activity Ingresar o Login
    private void openRegistrarlbl(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

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


} //fin Registrar