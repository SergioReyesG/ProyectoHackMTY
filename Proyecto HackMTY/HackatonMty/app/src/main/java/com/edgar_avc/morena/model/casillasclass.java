package com.edgar_avc.morena.model;

public class casillasclass {
    String Tipo;
    String Tipo_cas;
    String Seccion;
    String ListaNominal;
    //String Estado;
    //String NumMunicipio;
    //String Localidad;
    String Ubicacion;
    String CodigoPostal;

    public casillasclass() {
    }

    public casillasclass(String tipo, String tipo_cas, String seccion, String listaNominal, String ubicacion, String codigoPostal) {
        Tipo = tipo;
        Tipo_cas = tipo_cas;
        Seccion = seccion;
        ListaNominal = listaNominal;
        Ubicacion = ubicacion;
        CodigoPostal = codigoPostal;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getTipo_cas() {
        return Tipo_cas;
    }

    public void setTipo_cas(String tipo_cas) {
        Tipo_cas = tipo_cas;
    }

    public String getSeccion() {
        return Seccion;
    }

    public void setSeccion(String seccion) {
        Seccion = seccion;
    }

    public String getListaNominal() {
        return ListaNominal;
    }

    public void setListaNominal(String listaNominal) {
        ListaNominal = listaNominal;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        CodigoPostal = codigoPostal;
    }

    @Override
    public String toString() {
        return "casillasclass{" +
                "Tipo='" + Tipo + '\'' +
                ", Tipo_cas='" + Tipo_cas + '\'' +
                ", Seccion='" + Seccion + '\'' +
                ", ListaNominal='" + ListaNominal + '\'' +
                ", Ubicacion='" + Ubicacion + '\'' +
                ", CodigoPostal='" + CodigoPostal + '\'' +
                '}';
    }

}
