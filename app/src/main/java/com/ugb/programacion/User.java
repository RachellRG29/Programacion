package com.ugb.programacion;

public class User {
    public String usuario, correo;
    public boolean isAdmin;
    public String contrasena, contrasenaAdmin, adminPassword;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String correo, boolean isAdmin) {
        this.correo = correo;
        this.isAdmin = isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}

