package com.ugb.programacion;
public class amigos {
    String idAmigo;
    String nombreuser;
    String direccion;
    String telefono;
    String email;
    String dui;
    String imgusuario;
    String imgusuariosFirebaseurl;
    String token;

    public amigos(){}

    public amigos(String idAmigo, String nombreuser, String direccion, String telefono,
                  String email, String dui, String imgusuario, String imgusuariosFirebaseurl,String token) {
        this.idAmigo = idAmigo;
        this.nombreuser = nombreuser;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.dui = dui;
        this.imgusuario = imgusuario;
        this.imgusuariosFirebaseurl = imgusuariosFirebaseurl;
        this.token = token;

    }


    public String getIdAmigo() {
        return idAmigo;
    }

    public void setIdAmigo(String idAmigo) {
        this.idAmigo = idAmigo;
    }

    public String getNombreuser() {
        return nombreuser;
    }

    public void setNombreuser(String nombreuser) {
        this.nombreuser = nombreuser;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getImgusuario() {
        return imgusuario;
    }

    public void setImgusuario(String imgusuario) {
        this.imgusuario = imgusuario;
    }

    public String getImgusuariosFirebaseurl() {
        return imgusuariosFirebaseurl;
    }

    public void setImgusuariosFirebaseurl(String imgusuariosFirebaseurl) {
        this.imgusuariosFirebaseurl = imgusuariosFirebaseurl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}