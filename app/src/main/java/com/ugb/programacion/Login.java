package com.ugb.programacion;

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

public class Login extends AppCompatActivity {

    //Nahun
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));

        //valores para usuario y contraseña
        EditText txtUsuarioL= (EditText)findViewById(R.id.txt_nombreusuario);
        EditText txtContrasenaL= (EditText)findViewById(R.id.txt_contrasena);

        //txt lbl_signup_aActivitySignup a Registrar en caso de no tener cuenta
        TextView lblRegistrar = (TextView) findViewById(R.id.lbl_login_aActivitySignup);
        lblRegistrar.setOnClickListener(v -> openRegistrarlbl());

        //boton ingresar
        Button btnPrincipal = (Button) findViewById(R.id.btn_ingresar);
        btnPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtUsuarioL.getText().toString().isEmpty() || txtContrasenaL.getText().toString().isEmpty()){
                    txtUsuarioL.setError("Campo requerido");
                    txtContrasenaL.setError("Campo requerido");
                    return;

                } else {
                    irLista();
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

    //abrir view main activity Registrar o Sign up
    public void openRegistrarlbl(){
        Intent intent = new Intent(this, RegisterActivity.class);
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






} //fin login