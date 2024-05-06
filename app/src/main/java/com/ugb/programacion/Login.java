package com.ugb.programacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    //Nahun
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //valores para usuario y contraseña
        EditText txtUsuarioL= (EditText)findViewById(R.id.txt_usuario_login);
        EditText txtContrasenaL= (EditText)findViewById(R.id.txt_contrasena_login);

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
                    openLista();
                }
            }
        });


    } //fin oncreate

    //Private voids

    //abrir la lista principal
    private void openLista(){
        Intent abrirVentana = new Intent(getApplicationContext(), lista_delivery.class);
        startActivity(abrirVentana);

    }


} //fin login