package com.example.elperlanegra.modelos;

public class UserModel {
    String nombreAp;
    String direccion;
    String telefono;
    String email;
    String password;
    String fotoPerfil;


    public UserModel() {
    }

    public UserModel(String nombreAp, String direccion, String telefono, String email, String password) {
        this.nombreAp = nombreAp;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.password = password;

    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombreAp() {
        return nombreAp;
    }

    public void setNombreAp(String nombreAp) {
        this.nombreAp = nombreAp;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
