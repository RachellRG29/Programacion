package com.ugb.programacion;

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
                    // Validar usuario y contraseña
                    String usuario = userlogin.getText().toString();
                    String contrasena = passwordlogin.getText().toString();
                    if(validarCredenciales(usuario, contrasena)){
                        // Credenciales válidas, ir a la lista de consolas
                        mostrarMsg("Bienvenido!");
                        irLista();
                    } else {
                        // Credenciales inválidas, se muestra un mensaje de error por el usuario o contrasena
                        Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                    //irLista();
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