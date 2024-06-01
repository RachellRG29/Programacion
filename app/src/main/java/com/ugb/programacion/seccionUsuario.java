package com.ugb.programacion;

import android.content.Context;
import android.content.SharedPreferences;
public class seccionUsuario {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER_LOGGED_IN = "usuario_logeado";
    private static final String KEY_IS_ADMIN = "is_admin";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public seccionUsuario(Context context){
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String username, boolean isAdmin) {
        editor.putString(KEY_USER_LOGGED_IN, username);
        editor.putBoolean(KEY_IS_ADMIN, isAdmin);
        editor.commit();
    }

    public String getUsername() {
        return pref.getString(KEY_USER_LOGGED_IN, null);
    }

    public boolean isAdmin() {
        return pref.getBoolean(KEY_IS_ADMIN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }


}
