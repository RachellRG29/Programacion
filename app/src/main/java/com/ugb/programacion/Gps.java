package com.ugb.programacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Gps extends AppCompatActivity {
    //VARIABLES
    //Navegation bar
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));

        // Inicializar seccion usuario
        seccionUsuario seccionUsuario = new seccionUsuario(getApplicationContext());

        // Utiliza la información de sesión según sea necesario
        String usuarioLogeado = seccionUsuario.getUsername();
        boolean isAdmin = seccionUsuario.isAdmin();


        //BottomNavegation
        bottomNavigationView = findViewById(R.id.bottomNavegation);
        bottomNavigationView.setSelectedItemId(R.id.navGps);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navPrincipal:
                        // Acciones para Principal
                        irVista();
                        finish();
                        return true;

                    case R.id.navGps:
                        return true;

                    case R.id.navMensajeria:
                        startActivity(new Intent(getApplicationContext(), lista_usuarios.class));
                        finish();
                        return true;
                }
                return false;
            }
        }); // fin navigation

    }

    //Boton navegation
    private void irVista(){ //ir o regresar a la lista
        Intent abrirVentana = new Intent(getApplicationContext(), lista_delivery.class);
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

}
