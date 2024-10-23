package com.mygdx.game.items;

public class Llave extends Item {
    private int idLlave;
    private int idPuerta;

    public Llave(int code, String nombre, String descripcion, String rareza, int value, int durabilidad,
            boolean obtenido, int idLlave, int idPuerta) {
        super(code, descripcion, rareza, value, durabilidad, nombre, obtenido);
        this.idLlave = idLlave;
        this.idPuerta = idPuerta;
    }

    public void abrirPuerta() {
        
    }

    public int getIdLlave() {
        return idLlave;
    }

    public void setIdLlave(int idLlave) {
        this.idLlave = idLlave;
    }

    public int getIdPuerta() {
        return idPuerta;
    }

    public void setIdPuerta(int idPuerta) {
        this.idPuerta = idPuerta;
    }
    @Override
    public String toString() {
        return super.toString() + "%" + idLlave + "%" + idPuerta;
    }
}