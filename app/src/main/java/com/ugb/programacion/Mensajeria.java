package com.ugb.programacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//Yarit y karen
public class Mensajeria extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

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


} //fin mensajeria
