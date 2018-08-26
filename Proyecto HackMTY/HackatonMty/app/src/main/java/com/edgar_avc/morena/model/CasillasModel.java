package com.edgar_avc.morena.model;

public class CasillasModel {
    String codigoPostal;
    String listaNominal;
    String seccion;
    String tipo;
    String tipo_cas;
    String ubicacion;

    public CasillasModel(String codigoPostal, String listaNominal, String seccion, String tipo, String tipo_cas, String ubicacion) {
        this.codigoPostal = codigoPostal;
        this.listaNominal = listaNominal;
        this.seccion = seccion;
        this.tipo = tipo;
        this.tipo_cas = tipo_cas;
        this.ubicacion = ubicacion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getListaNominal() {
        return listaNominal;
    }

    public void setListaNominal(String listaNominal) {
        this.listaNominal = listaNominal;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo_cas() {
        return tipo_cas;
    }

    public void setTipo_cas(String tipo_cas) {
        this.tipo_cas = tipo_cas;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
