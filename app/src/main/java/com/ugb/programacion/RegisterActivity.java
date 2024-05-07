package com.ugb.programacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/* firebase
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
*/

public class RegisterActivity extends AppCompatActivity {
    //FirebaseFirestore mFirestore;
   // FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.grey));

       // mFirestore = FirebaseFirestore.getInstance();
       // mAuth = FirebaseAuth.getInstance();

        //txt lbl_signup_aActivitySignup a Registrar en caso de no tener cuenta
        TextView lblIngresar = (TextView) findViewById(R.id.lbl_signup_aActivityLogin);
        lblIngresar.setOnClickListener(v -> openRegistrarlbl());

        //Valores usuario, contraseña y email
        EditText name = (EditText)findViewById(R.id.nombre);
        EditText email = (EditText)findViewById(R.id.correo);
        EditText password = (EditText)findViewById(R.id.contrasena);


        //Boton para registrar
        Button btn_register = findViewById(R.id.btn_registro);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameUser = name.getText().toString().trim();
                String emailUser = email.getText().toString().trim();
                String passUser = password.getText().toString().trim();

                if (name.getText().toString().isEmpty() || nameUser.isEmpty()
                        || email.getText().toString().isEmpty() ||emailUser.isEmpty()
                        || password.getText().toString().isEmpty()  || passUser.isEmpty()   ) {
                    name.setError("Campo requerido");
                    email.setError("Campo requerido");
                    password.setError("Campo requerido");
                    return;

                } else {
                    irLogin();
                    //registerUser(nameUser, emailUser, passUser); firebase
                }
            }
        });


    } //onCreate

/* firebase
    private void registerUser(String nameUser, String emailUser, String passUser) {
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

    } */

    //Private void

    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    //ya habido registrado, ir a la pantalla principal de la lista delivery
    private void irLogin(){ //
        Intent abrirVentana = new Intent(getApplicationContext(), lista_delivery.class);
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