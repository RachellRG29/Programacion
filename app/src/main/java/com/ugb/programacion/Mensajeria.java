package com.ugb.programacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//Yarit y karen hacer mensajeria.
public class Mensajeria extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));

        bottomNavigationView = findViewById(R.id.bottomNavegation);
        bottomNavigationView.setSelectedItemId(R.id.navMensajeria);

        bottomNavigationView.setOnItemReselectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navPrincipal: //Lista tulip
                    startActivity(new Intent(getApplicationContext(), lista_delivery.class));
                    finish();
                    return;
                case R.id.navAgregar: //Agregar tulip
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    return;
                case R.id.navMensajeria: //Mensajeria
                    return;
            }
        }); //fin navegation



    }//fin onCreate

    //Private voids



//Usar LA PALETA DE COLOR SUGERIDA
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
