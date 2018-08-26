package com.edgar_avc.morena.model;

public class usuarios {

    String ap_mat;
    String ap_pat;
    String calle;
    String clave_elec;
    String codPostal;
    String curp;
    String noExterior;
    String noInterior;
    String nomLocalidad;
    String nomMunicipio;
    String nombre;
    String ocr;
    String password;
    String referente;
    String tel;
    String tipoEstructura;

    public usuarios() {
    }

    public usuarios(String ap_mat, String ap_pat, String calle, String clave_elec, String codPostal, String curp, String noExterior, String noInterior, String nomLocalidad, String nomMunicipio, String nombre, String ocr, String password, String referente, String tel, String tipoEstructura) {
        this.ap_mat = ap_mat;
        this.ap_pat = ap_pat;
        this.calle = calle;
        this.clave_elec = clave_elec;
        this.codPostal = codPostal;
        this.curp = curp;
        this.noExterior = noExterior;
        this.noInterior = noInterior;
        this.nomLocalidad = nomLocalidad;
        this.nomMunicipio = nomMunicipio;
        this.nombre = nombre;
        this.ocr = ocr;
        this.password = password;
        this.referente = referente;
        this.tel = tel;
        this.tipoEstructura = tipoEstructura;
    }

    public String getAp_mat() {
        return ap_mat;
    }

    public void setAp_mat(String ap_mat) {
        this.ap_mat = ap_mat;
    }

    public String getAp_pat() {
        return ap_pat;
    }

    public void setAp_pat(String ap_pat) {
        this.ap_pat = ap_pat;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getClave_elec() {
        return clave_elec;
    }

    public void setClave_elec(String clave_elec) {
        this.clave_elec = clave_elec;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNoExterior() {
        return noExterior;
    }

    public void setNoExterior(String noExterior) {
        this.noExterior = noExterior;
    }

    public String getNoInterior() {
        return noInterior;
    }

    public void setNoInterior(String noInterior) {
        this.noInterior = noInterior;
    }

    public String getNomLocalidad() {
        return nomLocalidad;
    }

    public void setNomLocalidad(String nomLocalidad) {
        this.nomLocalidad = nomLocalidad;
    }

    public String getNomMunicipio() {
        return nomMunicipio;
    }

    public void setNomMunicipio(String nomMunicipio) {
        this.nomMunicipio = nomMunicipio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOcr() {
        return ocr;
    }

    public void setOcr(String ocr) {
        this.ocr = ocr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReferente() {
        return referente;
    }

    public void setReferente(String referente) {
        this.referente = referente;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTipoEstructura() {
        return tipoEstructura;
    }

    public void setTipoEstructura(String tipoEstructura) {
        this.tipoEstructura = tipoEstructura;
    }

    @Override
    public String toString() {
        return "usuarios{" +
                "ap_mat='" + ap_mat + '\'' +
                ", ap_pat='" + ap_pat + '\'' +
                ", calle='" + calle + '\'' +
                ", clave_elec='" + clave_elec + '\'' +
                ", codPostal='" + codPostal + '\'' +
                ", curp='" + curp + '\'' +
                ", noExterior='" + noExterior + '\'' +
                ", noInterior='" + noInterior + '\'' +
                ", nomLocalidad='" + nomLocalidad + '\'' +
                ", nomMunicipio='" + nomMunicipio + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ocr='" + ocr + '\'' +
                ", password='" + password + '\'' +
                ", referente='" + referente + '\'' +
                ", tel='" + tel + '\'' +
                ", tipoEstructura='" + tipoEstructura + '\'' +
                '}';
    }
}
