package com.edgar_avc.morena.model;

public class RelacionCasilla {
    String colaborador;
    String Casilla;

    public RelacionCasilla(String colaborador, String casilla) {
        this.colaborador = colaborador;
        Casilla = casilla;
    }

    public String getColaborador() {
        return colaborador;
    }

    public void setColaborador(String colaborador) {
        this.colaborador = colaborador;
    }

    public String getCasilla() {
        return Casilla;
    }

    public void setCasilla(String casilla) {
        Casilla = casilla;
    }
}
