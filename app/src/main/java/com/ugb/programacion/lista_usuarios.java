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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

//aqui esta el activity que se regitraron los usuarios
public class lista_usuarios extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton btnAgregarUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));

        btnAgregarUser = findViewById(R.id.btnAgregarUsuario);
        btnAgregarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAgregarUser();
            }
        });


        //BottomNavegation
        bottomNavigationView = findViewById(R.id.bottomNavegation);
        bottomNavigationView.setSelectedItemId(R.id.navMensajeria);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navPrincipal:
                        // Acciones para Principal
                        startActivity(new Intent(getApplicationContext(), lista_delivery.class));
                        finish();
                        return true;

                    case R.id.navGps:
                        startActivity(new Intent(getApplicationContext(), Gps.class));
                        finish();
                        return true;

                    case R.id.navMensajeria:
                        return true;
                }
                return false;
            }
        }); // fin navigation


    }


    private void irAgregarUser(){
        Intent abrirVentana = new Intent(getApplicationContext(), Usuarios.class);
        startActivity(abrirVentana);

    }

    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void cambiarColorBarraEstado(int color) {
        // Verificar si la versión del SDK es Lollipop o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    } //fin cambiar colorbarraestado


}