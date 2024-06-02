package com.ugb.programacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

//aqui esta el activity que se regitraron los usuarios
public class lista_usuarios extends AppCompatActivity {

    seccionUsuario session;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton btnAgregarUser;
    Bundle parametrosuser = new Bundle();
    ListView ltsUser;
    Cursor cAmigos;
    DB_user dbAmigos;
    amigos misAmigos;
    DatabaseReference databaseReference;
    final ArrayList<amigos> alAmigos=new ArrayList<amigos>();
    final ArrayList<amigos> alAmigosCopy=new ArrayList<amigos>();
    JSONArray datosUserJSON = new JSONArray();
    JSONObject jsonUserObject;
    detectarInternet di;
    int posicion=0;
    String miToken="";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));


        ltsUser = findViewById(R.id.ltsAmigos);

        btnAgregarUser = findViewById(R.id.btnAgregarUsuario);
        btnAgregarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAgregarUser();
            }
        });

        try{
            di = new detectarInternet(getApplicationContext());
            if(di.hayConexionInternet()){//online
                obtenerDatosAmigosServidor();
                sincronizar();
            }else{
                obtenerAmigos();//offline
            }
        }catch (Exception e){
            mostrarMsg("Error al detectar si hay conexion "+ e.getMessage());
        }
        buscarAmigos();
        mostrarChats();


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


    //private voids


    private void sincronizar(){
        try{
            /*cAmigos = dbAmigos.pendienteSincronizar();
            if( cAmigos.moveToFirst() ){//hay registros pendientes de sincronizar
                jsonObject = new JSONObject();
                mostrarMsg("Sincronizando...");

                do{
                    if( cAmigos.getString(0).length()>0 && cAmigos.getString(1).length()>0 ){
                        jsonObject.put("_id", cAmigos.getString(0));
                        jsonObject.put("_rev", cAmigos.getString(1));
                    }
                    jsonObject.put("idAmigo", cAmigos.getString(2));
                    jsonObject.put("nombre", cAmigos.getString(3));
                    jsonObject.put("direccion", cAmigos.getString(4));
                    jsonObject.put("telefono", cAmigos.getString(5));
                    jsonObject.put("email", cAmigos.getString(6));
                    jsonObject.put("dui", cAmigos.getString(7));
                    jsonObject.put("urlCompletaFoto", cAmigos.getString(8));
                    jsonObject.put("actualizado", "si");

                    enviarDatosServidor objGuardarDatosServidor = new enviarDatosServidor(getApplicationContext());
                    String respuesta = objGuardarDatosServidor.execute(jsonObject.toString()).get();

                    JSONObject respuestaJSONObject = new JSONObject(respuesta);
                    if (respuestaJSONObject.getBoolean("ok")) {
                        DB db = new DB(getApplicationContext(), "", null, 1);
                        String[] datos = new String[]{
                                respuestaJSONObject.getString("id"),
                                respuestaJSONObject.getString("rev"),
                                jsonObject.getString("idAmigo"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("direccion"),
                                jsonObject.getString("telefono"),
                                jsonObject.getString("email"),
                                jsonObject.getString("dui"),
                                jsonObject.getString("urlCompletaFoto"),
                                jsonObject.getString("actualizado")
                        };
                        respuesta = db.administrar_amigos("modificar", datos);
                        if (!respuesta.equals("ok")) {
                            mostrarMsg(respuesta);
                        }
                    } else {
                        mostrarMsg("Error al sincronizar en servidor: " + respuesta);
                    }
                }while (cAmigos.moveToNext());
            }*/
        }catch (Exception e){
            mostrarMsg("Error al sincronizar: "+ e.getMessage());
        }
    }


    private void obtenerAmigos(){
        try{
            cAmigos = dbAmigos.consultar_amigos();
            if ( cAmigos.moveToFirst() ){
                datosUserJSON = new JSONArray();
                do{
                    jsonUserObject = new JSONObject();
                    JSONObject jsonObjectValue = new JSONObject();
                    jsonUserObject.put("_id",cAmigos.getString(0));
                    jsonUserObject.put("_rev",cAmigos.getString(1));
                    jsonUserObject.put("idAmigo",cAmigos.getString(2));
                    jsonUserObject.put("nombre",cAmigos.getString(3));
                    jsonUserObject.put("direccion",cAmigos.getString(4));
                    jsonUserObject.put("telefono",cAmigos.getString(5));
                    jsonUserObject.put("email",cAmigos.getString(6));
                    jsonUserObject.put("dui",cAmigos.getString(7));
                    jsonUserObject.put("imgusuario",cAmigos.getString(8));

                    jsonObjectValue.put("value", jsonUserObject);
                    datosUserJSON.put(jsonObjectValue);
                }while(cAmigos.moveToNext());
            }else{
                mostrarMsg("No hay amigos que mostrar");
            }
        }catch (Exception e){
            mostrarMsg("Error al obtener amigos: "+ e.getMessage());
        }
    }

    private void mostrarDatosAmigos(){
        try{
            if( datosUserJSON.length()>0 ){
                alAmigos.clear();
                alAmigosCopy.clear();

                JSONObject misDatosJSONObject;
                for (int i=0; i<datosUserJSON.length(); i++){
                    misDatosJSONObject = datosUserJSON.getJSONObject(i);
                    misAmigos = new amigos(
                            misDatosJSONObject.getString("idAmigo"),
                            misDatosJSONObject.getString("nombre"),
                            misDatosJSONObject.getString("direccion"),
                            misDatosJSONObject.getString("telefono"),
                            misDatosJSONObject.getString("email"),
                            misDatosJSONObject.getString("dui"),
                            misDatosJSONObject.getString("imgusuario"),
                            misDatosJSONObject.getString("imgusuariosFirebaseurl"),
                            misDatosJSONObject.getString("to")
                    );
                    alAmigos.add(misAmigos);
                }
                adaptadorUsuarios adImagenesuser = new adaptadorUsuarios(getApplicationContext(), alAmigos);
                ltsUser.setAdapter(adImagenesuser);
                alAmigosCopy.addAll(alAmigos);

                registerForContextMenu(ltsUser);
            }else{
                mostrarMsg("No hay datos que mostrar.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar los datos: "+e.getMessage());
        }
    }

    private void obtenerDatosAmigosServidor(){
        try{
            databaseReference = FirebaseDatabase.getInstance().getReference("amigos");
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(tarea->{
                if( !tarea.isSuccessful() ){
                    return;
                }
                miToken = tarea.getResult();
                if( miToken!=null && miToken.length()>0 ){
                    databaseReference.orderByChild("token").equalTo(miToken).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try{
                                if(snapshot.getChildrenCount()<=0){
                                    mostrarMsg("Usuario no registrado...");
                                    parametrosuser.putString("accion", "nuevo");
                                    abrirActividad(parametrosuser);
                                }
                            }catch (Exception e){
                                mostrarMsg("Error al obtener mis datos: "+ e.getMessage());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            mostrarMsg("Accion cancelado...");
                        }
                    });
                }
            });
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            amigos amigo = dataSnapshot.getValue(amigos.class);
                            jsonUserObject = new JSONObject();
                            jsonUserObject.put("idAmigo", amigo.getIdAmigo());
                            jsonUserObject.put("nombre", amigo.getNombreuser());
                            jsonUserObject.put("direccion", amigo.getDireccion());
                            jsonUserObject.put("telefono", amigo.getTelefono());
                            jsonUserObject.put("email", amigo.getEmail());
                            jsonUserObject.put("dui", amigo.getDui());
                            jsonUserObject.put("imgusuario", amigo.getImgusuario());
                            jsonUserObject.put("imgusuariosFirebaseurl", amigo.getImgusuariosFirebaseurl());
                            jsonUserObject.put("to", amigo.getToken());
                            jsonUserObject.put("from", miToken);
                            datosUserJSON.put(jsonUserObject);
                        }
                        mostrarDatosAmigos();
                    }catch (Exception e){
                        mostrarMsg("Error al obtener datos de usuarios: "+ e.getMessage());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            mostrarMsg("Error al obtener datos desde el servidor: "+ e.getMessage());
        }
    }
    private void mostrarChats(){
        ltsUser.setOnItemClickListener((parent, view, position, id) -> {
            try{
                Bundle bundle = new Bundle();
                bundle.putString("nombreuser", datosUserJSON.getJSONObject(position).getString("nombreuser") );
                bundle.putString("to", datosUserJSON.getJSONObject(position).getString("to") );
                bundle.putString("from", datosUserJSON.getJSONObject(position).getString("from") );
                bundle.putString("imgusuario", datosUserJSON.getJSONObject(position).getString("imgusuario") );
                bundle.putString("imgusuariosFirebaseurl", datosUserJSON.getJSONObject(position).getString("imgusuariosFirebaseurl") );

                Intent intent = new Intent(getApplicationContext(), Usuarios.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }catch (Exception ex){
                mostrarMsg(ex.getMessage());
            }
        });
    }


    private void buscarAmigos() {
        TextView tempVal;
        tempVal = findViewById(R.id.txtBuscarAmigos);
        tempVal.addTextChangedListener(new TextWatcher() {
            private Editable s;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    alAmigos.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if (valor.length() <= 0) {
                        alAmigos.addAll(alAmigosCopy);
                    } else {
                        for (amigos amigo : alAmigosCopy) {
                            String nombreuser = amigo.getNombreuser();
                            String direccion = amigo.getDireccion();
                            String tel = amigo.getTelefono();
                            String email = amigo.getEmail();
                            if (nombreuser.toLowerCase().trim().contains(valor) ||
                                    direccion.toLowerCase().trim().contains(valor) ||
                                    tel.trim().contains(valor) ||
                                    email.trim().toLowerCase().contains(valor)) {
                                alAmigos.add(amigo);
                            }
                        }
                        adaptadorUsuarios adImagenesuser = new adaptadorUsuarios(getApplicationContext(), alAmigos);
                        ltsUser.setAdapter(adImagenesuser);
                    }
                } catch (Exception e) {
                    mostrarMsg("Error al buscar: " + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void abrirActividad(Bundle parametros){
        Intent abriVentana = new Intent(getApplicationContext(), lista_usuarios.class);
        abriVentana.putExtras(parametros);
        startActivity(abriVentana);
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