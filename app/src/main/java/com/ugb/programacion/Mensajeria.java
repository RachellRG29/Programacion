package com.ugb.programacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

//Chast de los usuarios
public class Mensajeria extends AppCompatActivity {
    seccionUsuario session;
    ImageView imgAtras;
    CircleImageView imgTemp;
    TextView tempVal;
    String to="", from="", user="", msg = "", urlPhoto = "", urlPhotoFirestore = "";
    DatabaseReference databaseReference;
    private chatsArrayAdapter chatArrayAdapter;
    TextView txtMsg;
    ListView ltsChats;
    Button btnEnviar;
    TextView lblusuariotexto;

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(MyFirebaseMessagingService.DISPLAY_MESSAGE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(notificacionPush, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificacionPush);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);


        //cambiar color barra estado
        cambiarColorBarraEstado(getResources().getColor(R.color.darkblue));

        // Inicializar seccion usuario
        seccionUsuario seccionUsuario = new seccionUsuario(getApplicationContext());

        String usuarioLogeado = getIntent().getStringExtra("usuario_logeado");
        boolean isAdmin = getIntent().getBooleanExtra("is_admin", false);

        if (usuarioLogeado != null) {
            session.createLoginSession(usuarioLogeado, isAdmin);
            //lblusuariotexto.setText(usuarioLogeado);
        }

        try {
            tempVal = findViewById(R.id.lblToChat);
            //Regresar atras a la lista de usuarios o amigos
            imgAtras= findViewById(R.id.imgAtras);
            imgAtras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    irLista();
                }
            });
            Bundle parametros = getIntent().getExtras();
            if (parametros.getString("to") != null && parametros.getString("to") != "") {
                to = parametros.getString("to");
                from = parametros.getString("from");
                user = parametros.getString("nombreuser");
                urlPhoto = parametros.getString("imgusuariourl");
                urlPhotoFirestore = parametros.getString("getimgusuariosFirebaseurl");
                tempVal.setText(user);
            }
            txtMsg = findViewById(R.id.txtMsg);
            txtMsg.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    try {
                        guardarMsgFirebsae(txtMsg.getText().toString());
                        sendChatMessage(false, txtMsg.getText().toString());
                    }catch (Exception e){
                        mostrarMsg(e.getMessage());
                    }
                }
                return false;
            });
            btnEnviar = findViewById(R.id.btnEnviarMsg);
            btnEnviar.setOnClickListener(v -> {
                try {
                    guardarMsgFirebsae(txtMsg.getText().toString());
                    sendChatMessage(false, txtMsg.getText().toString());
                }catch (Exception e){
                    mostrarMsg(e.getMessage());
                }
            });
            ltsChats = findViewById(R.id.ltsChats);

            chatArrayAdapter = new chatsArrayAdapter(getApplicationContext(), R.layout.msgizquierdo);
            ltsChats.setAdapter(chatArrayAdapter);
        }catch (Exception e){
            mostrarMsg(e.getMessage());
        }
        mostrarFoto();
        historialMsg();




    }//fin onCreate

    //Private voids

    void mostrarFoto(){
        imgTemp = findViewById(R.id.imgFotoAmigo);
        Glide.with(getApplicationContext()).load(urlPhotoFirestore).into(imgTemp);
    }

    void sendChatMessage(Boolean posicion, String msg){
        try{
            chatArrayAdapter.add(new chatMessage(posicion, msg));
            txtMsg.setText("");
        }catch (Exception e){
            mostrarMsg("Error al enviar el mensje al chats: "+ e.getMessage());
        }
    }
    void guardarMsgFirebsae(String msg){
        try {
            JSONObject data = new JSONObject();
            data.put("para", to);
            data.put("de", from);
            data.put("msg", msg);
            data.put("nombreuser", user);

            JSONObject notificacion = new JSONObject();
            notificacion.put("title", "Mensaje de "+ user);
            notificacion.put("body", data);

            JSONObject miData = new JSONObject();
            miData.put("to", to);
            miData.put("notification", notificacion);
            miData.put("data", data);

            //enviar msj via webservice
            enviarDatos objEnviar = new enviarDatos();
            objEnviar.execute(miData.toString());

            //guardar en firebase
            chats_mensajes chatsMsg = new chats_mensajes(to, from, to + "_"+from, msg);
            String key = databaseReference.push().getKey();
            databaseReference.child(key).setValue(chatsMsg);
        }catch (Exception e){
            mostrarMsg("Error al intentar guardar el msg en Firebase: "+ e.getMessage());
        }
    }
    private class enviarDatos extends AsyncTask<String,String,String> {
        HttpURLConnection urlConnection;
        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            String JsonResponse = null;
            String JsonDATA = params[0];
            BufferedReader reader = null;

            try {
                //conexion al servidor...
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization", "key=BCUscPfOC1we5f8w_rg1Hw9yl5CfVR4c_CofDMAH1B9Fx_S9MdDi3l-WNaxheJt0gz7vtfWKiruJkcP9SCgu3BE");

                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                writer.close();

                // json data
                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                StringBuffer buffer = new StringBuffer();
                while ((inputLine = reader.readLine()) != null) {
                    buffer.append(inputLine + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                JsonResponse = buffer.toString();
                return JsonResponse;

            }catch (Exception ex){
                ex.printStackTrace();
                Log.d("URI: ", "Error enviando notificacion: "+ ex.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if( s!=null ) {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("success") <= 0) {
                        mostrarMsg("Error al enviar mensaje");
                    }
                }
            }catch(Exception ex){
                mostrarMsg("Error al enviar a la red: "+ ex.getMessage());
            }
        }
    }
    void historialMsg(){
        databaseReference = FirebaseDatabase.getInstance().getReference("chats");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.getChildrenCount()>0 ){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if( (dataSnapshot.child("de").getValue().equals(from) && dataSnapshot.child("para").getValue().equals(to))
                                || (dataSnapshot.child("de").getValue().equals(to) && dataSnapshot.child("para").getValue().equals(from))) {
                            sendChatMessage(dataSnapshot.child("para").getValue().equals(from), dataSnapshot.child("msg").getValue().toString());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private BroadcastReceiver notificacionPush = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WakeLocker.acquire(getApplicationContext());

            msg = intent.getStringExtra("msg");
            to = intent.getStringExtra("from");
            from = intent.getStringExtra("to");

            sendChatMessage(true, msg);
            WakeLocker.release();
        }
    };

    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

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
