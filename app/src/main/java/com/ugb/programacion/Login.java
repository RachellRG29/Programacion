package com.ugb.programacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText userlogin, passwordlogin;
    private DB_register db_register;
    private FirebaseAuth mAuth;
    private static final String ADMIN_USER = "Admin1";
    private static final String ADMIN_PASSWORD = "123456";
    private static final String ADMIN_EMAIL = "admin01@ugb.edu.sv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));

        db_register = new DB_register(this, "", null, 1);

        userlogin = findViewById(R.id.txtUsuarioLogin);
        passwordlogin = findViewById(R.id.txtConstrasenaLogin);

        TextView lblRegistrar = findViewById(R.id.lbl_login_aActivitySignup);
        lblRegistrar.setOnClickListener(v -> openRegistrarlbl());

        Button btnPrincipal = findViewById(R.id.btn_ingresar);
        btnPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userlogin.getText().toString().isEmpty() || passwordlogin.getText().toString().isEmpty()){
                    userlogin.setError("Campo requerido");
                    passwordlogin.setError("Campo requerido");
                    return;

                } else {
                    String usuario = userlogin.getText().toString();
                    String contrasena = passwordlogin.getText().toString();
                    boolean isAdmin = usuario.equals(ADMIN_USER) && contrasena.equals(ADMIN_PASSWORD);
                    // Validar credenciales localmente
                    if(validarCredenciales(usuario, contrasena)){
                        // Credenciales válidas localmente, intentar iniciar sesión
                        iniciarSesion(usuario, contrasena);
                        irLista(usuario, isAdmin);
                    } else {
                        //en caso de fallar, si usuario o la contra es erronea
                        Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    // Método para validar credenciales localmente
    private boolean validarCredenciales(String usuario, String contrasena) {
        SQLiteDatabase db = db_register.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM register WHERE usuario = ? AND contrasena = ?", new String[]{usuario, contrasena});
        boolean isValid = cursor.moveToFirst();
        cursor.close();
        return isValid;
    }

    // Método para abrir la actividad de registrar
    public void openRegistrarlbl(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    // Método para mostrar mensajes
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    // Método para cambiar el color de la barra de estado
    private void cambiarColorBarraEstado(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    // Método para iniciar sesión
    private void iniciarSesion(String usuario, String contrasena) {
        if (NetworkUtil.isNetworkAvailable(this)) {
            //hay conexion a interner, guardar con firebase
            signInFirebase(usuario, contrasena);
        } else {
            // No hay conexión a internet, guardar localmente
            guardarLocalmente(usuario, contrasena);
        }
    }

    // Método para iniciar sesión con Firebase
    private void signInFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión con Firebase exitoso!!!
                            FirebaseUser user = mAuth.getCurrentUser();
                            mostrarMsg("Inicio de sesión con Firebase exitoso");
                            boolean isAdmin = email.equals(ADMIN_EMAIL);
                            irLista(email, isAdmin);
                        } /*else {
                            // Error al iniciar sesión con Firebase, mostrar mensaje de error
                           // mostrarMsg("Error en Firebase: " + task.getException().getMessage());
                        }*/
                    }
                });
    }

    // Método para guardar los datos localmente
    private void guardarLocalmente(String usuario, String contrasena) {
        mostrarMsg("Bienvenido!...");
    }

    // Método para abrir la actividad de la lista

    private void irLista(String usuario, boolean isAdmin){
        Intent abrirVentana = new Intent(getApplicationContext(), lista_delivery.class);
        abrirVentana.putExtra("usuario_logeado", usuario);
        abrirVentana.putExtra("is_admin", isAdmin);
        startActivity(abrirVentana);
    }

   /* private void irLista(){
        Intent abrirVentana = new Intent(getApplicationContext(), lista_delivery.class);
        startActivity(abrirVentana);
    }*/

} //Fin login :3



/*package com.ugb.programacion; //codigo anterior solo local

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText userlogin, passwordlogin;
    private DB_register db_register; // base de datos de los usuarios registrados
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));

        // iniciarlizar la base de datos
        db_register = new DB_register(this, "", null, 1);

        //valores para usuario y contraseña
        userlogin= (EditText)findViewById(R.id.txtUsuarioLogin);
        passwordlogin= (EditText)findViewById(R.id.txtConstrasenaLogin);

        //txt lbl_signup_aActivitySignup a Registrar en caso de no tener cuenta
        TextView lblRegistrar = (TextView) findViewById(R.id.lbl_login_aActivitySignup);
        lblRegistrar.setOnClickListener(v -> openRegistrarlbl());

        //boton ingresar
        Button btnPrincipal = (Button) findViewById(R.id.btn_ingresar);
        btnPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //campos obligatorios
                if(userlogin.getText().toString().isEmpty() || passwordlogin.getText().toString().isEmpty()){
                    userlogin.setError("Campo requerido");
                    passwordlogin.setError("Campo requerido");
                    return;

                } else {
                    // Validar usuario y contraseña local
                    String usuario = userlogin.getText().toString();
                    String contrasena = passwordlogin.getText().toString();
                    if(validarCredenciales(usuario, contrasena)){
                        // Credenciales válidas locales, ir a la lista de consolas
                        mostrarMsg("Bienvenido!");
                        irLista();
                    } else {
                        // Credenciales inválidas, se muestra un mensaje de error por el usuario o contrasena
                        Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    } //fin oncreate

    //Private voids

    //abrir la lista principal
    private void irLista(){
        Intent abrirVentana = new Intent(getApplicationContext(), lista_delivery.class);
        startActivity(abrirVentana);

    }

    // Método para validar las credenciales en la base de datos
    private boolean validarCredenciales(String usuario, String contrasena) {
        SQLiteDatabase db = db_register.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM register WHERE usuario = ? AND contrasena = ?", new String[]{usuario, contrasena});
        boolean isValid = cursor.moveToFirst(); // Verifica si hay al menos un resultado
        cursor.close();
        return isValid;
    }


    //abrir view main activity Registrar o Sign up
    public void openRegistrarlbl(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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


} //fin login
*/

