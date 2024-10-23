package com.mygdx.game.items;

public class Armadura extends Item {
    private int proteccion;

    public Armadura(int code, String nombre, String descripcion, String rareza, int value, int durabilidad,
            boolean obtenido, int proteccion) {
    	super(code, descripcion, rareza, value, durabilidad, nombre, obtenido);
    	this.proteccion = proteccion;
    }

    public int getProteccion() {
        return proteccion;
    }

    public void setProteccion(int proteccion) {
        this.proteccion = proteccion;
    }
    @Override
    public String toString() {
        return super.toString() + "%" + proteccion;
    }

    // Otros métodos específicos de la armadura
}
