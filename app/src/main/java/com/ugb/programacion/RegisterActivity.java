package com.ugb.programacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Firebase
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    TextView tempReg;
    String accion = "nuevo", idRegister = "";
    int posicion = 0;
    Bundle parametros = new Bundle();

    private EditText usuario, correo, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.red));
        idRegister = generarIdUnico();

        // Firebase
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // txt lbl_signup_aActivitySignup a Registrar en caso de no tener cuenta
        TextView lblIngresar = findViewById(R.id.lbl_register_aActivityLogin);
        lblIngresar.setOnClickListener(v -> openRegistrarlbl());

        // Valores usuario, contraseña y email
        usuario = findViewById(R.id.txtUsuarioRegister);
        correo = findViewById(R.id.txtCorreoRegister);
        contrasena = findViewById(R.id.txtContrasenaRegister);

        // Boton para registrar
        Button btn_register = findViewById(R.id.btn_registrar);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameUser = usuario.getText().toString().trim();
                String emailUser = correo.getText().toString().trim();
                String passUser = contrasena.getText().toString().trim();

                if (nameUser.isEmpty() || emailUser.isEmpty() || passUser.isEmpty()) {
                    if (nameUser.isEmpty()) {
                        usuario.setError("Campo requerido");
                    }
                    if (emailUser.isEmpty()) {
                        correo.setError("Campo requerido");
                    }
                    if (passUser.isEmpty()) {
                        contrasena.setError("Campo requerido");
                    }
                    return;
                }

                // Validar contraseña
                if (passUser.length() < 6) {
                    contrasena.setError("La contraseña debe tener al menos 6 caracteres");
                    return;
                }

                // Guardar local
                try {
                    DB_register db_register = new DB_register(getApplicationContext(), "", null, 1);
                    String[] datos = new String[]{idRegister, nameUser, emailUser, passUser};
                    String respuesta = db_register.administrar_register(accion, datos);
                    if (respuesta.equals("ok")) {
                        mostrarMsg("Usuario Registrado con Exito ");
                        registrarUsuario(nameUser, emailUser, passUser); // Firebase
                        irLogin();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error: " + respuesta, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    } // onCreate

    private void registrarUsuario(String usuario, String correo, String contrasena) {
        mAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();
                            Map<String, Object> user = new HashMap<>();
                            user.put("id", userId);
                            user.put("usuario", usuario);
                            user.put("correo", correo);

                            // Guardar en Realtime Database
                            mDatabase.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Guardar en Firestore
                                        mFirestore.collection("users").document(userId).set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        mostrarMsg("Usuario registrado con éxito");
                                                        irLogin(); // Ir login si se registró
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        mostrarMsg("Error al registrar en Firestore: " + e.getMessage());
                                                    }
                                                });
                                    } else {
                                        mostrarMsg("Error al registrar en Realtime Database: " + task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            mostrarMsg("Error en el Registro: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public String generarIdUnico() {
        return java.util.UUID.randomUUID().toString();
    }

    private void irLogin() {
        Intent abrirVentana = new Intent(getApplicationContext(), Login.class);
        startActivity(abrirVentana);
    }

    private void openRegistrarlbl() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private void cambiarColorBarraEstado(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
} // fin RegisterActivity


