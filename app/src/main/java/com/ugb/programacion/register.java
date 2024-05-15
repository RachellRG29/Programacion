package com.ugb.programacion;

public class register {

    String idRegister;
    String usuario;
    String correo;
    String contrasena;


    public register(String idRegister, String usuario, String correo, String contrasena){
        this.idRegister= idRegister;
        this.usuario = usuario;
        this.correo = correo;
        this.contrasena= contrasena;


    }

    public String getIdRegister() {
        return idRegister;
    }

    public void setIdRegister(String idRegister) {
        this.idRegister = idRegister;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }


}
