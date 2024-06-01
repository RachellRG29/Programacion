package com.ugb.programacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//Yarit y karen hacer mensajeria. este es el chats de los usuarios
public class Mensajeria extends AppCompatActivity {
    ImageView imgAtras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);


        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));

        // Inicializar seccion usuario
        seccionUsuario seccionUsuario = new seccionUsuario(getApplicationContext());


        //Regresar atras a la lista de usuarios o amigos
        imgAtras= findViewById(R.id.imgAtras);
        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irLista();
            }
        });




    }//fin onCreate

    //Private voids



    private void irLista(){ //ir o regresar a la lista de usuarios
        Intent abrirVentana = new Intent(getApplicationContext(), lista_usuarios.class);
        startActivity(abrirVentana);
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


} //fin mensajeria
