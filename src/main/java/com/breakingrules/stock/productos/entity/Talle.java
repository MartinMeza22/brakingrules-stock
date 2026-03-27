package com.breakingrules.stock.productos.entity;

public enum Talle {
    //Alfabéticos

    S,
    M,
    L,
    XL,
    XXL,
    XXXL,
    XXXXL,
    XXXXXL,
    XXXXXXL,

    //Númericos
    T38,
    T40,
    T42,
    T44,
    T46,
    T48,
    T50,
    T52;


    public boolean esNumerico() {
        return name().startsWith("T");
    }

    public boolean esAlfabetico() {
        return !esNumerico();
    }

    public String mostrar() {
        return esNumerico() ? name().substring(1) : name();
    }
}
