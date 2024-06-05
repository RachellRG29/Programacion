package com.ugb.programacion;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Gps extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView mensaje1;
    TextView mensaje2;
    Button btnMostrarMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        mensaje1 = findViewById(R.id.mensaje_id);
        mensaje2 = findViewById(R.id.mensaje_id2);
        btnMostrarMapa = findViewById(R.id.btnMostrarMapa);

        bottomNavigationView = findViewById(R.id.bottomNavegation);
        bottomNavigationView.setSelectedItemId(R.id.navGps);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navPrincipal:
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
        });

        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));

        if (ActivityCompat.checkSelfPermission(Gps.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Gps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            locationStart();
        }

        btnMostrarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Gps.this, MapsAcivity.class);
                startActivity(intent);
            }
        });
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion local = new Localizacion();
        local.setGPSActivity(Gps.this);

        if (mlocManager != null) {
            final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gpsEnabled) {
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingsIntent);
            }
            if (ActivityCompat.checkSelfPermission(Gps.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Gps.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Gps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
                return;
            }
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, local);
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, local);
            mensaje1.setText("Localización agregada");
            mensaje2.setText("");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
            } else {
                mensaje1.setText("Permiso denegado");
            }
        }
    }

    public void setLocation(Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    mensaje2.setText("Mi dirección es: \n" + DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void irVista() {
        Intent abrirVentana = new Intent(getApplicationContext(), lista_delivery.class);
        startActivity(abrirVentana);
    }

    private void cambiarColorBarraEstado(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public class Localizacion implements LocationListener {
        Gps gpsActivity;

        public void setGPSActivity(Gps gpsActivity) {
            this.gpsActivity = gpsActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            String Text = "Mi ubicación actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();
            mensaje1.setText(Text);
            gpsActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            mensaje1.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            mensaje1.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    break;
            }
        }
    }
}