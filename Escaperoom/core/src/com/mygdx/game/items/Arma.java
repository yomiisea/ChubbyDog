package com.mygdx.game.items;

public class Arma extends Item {
    private int danio;

    public Arma(int code, String nombre, String descripcion, String rareza, int value, int durabilidad,
            boolean obtenido, int danio) {
    super(code, descripcion, rareza, value, durabilidad, nombre, obtenido);
    this.danio = danio;
    }

    public int getDanio() {
        return danio;
    }

    public void setDanio(int danio) {
        this.danio = danio;
    }

    public void usarArma() {
        // Aquí puedes implementar la lógica correspondiente a usar el arma en el combate
        System.out.println("Has usado un arma. Causa " + danio + " de daño al enemigo.");
    }
    @Override
    public String toString() {
        return super.toString() + "%" + danio;
    }
}