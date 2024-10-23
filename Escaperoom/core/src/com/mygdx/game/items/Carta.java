package com.mygdx.game.items;

public class Carta extends Item {
    private String contenido;

    public Carta(int code, String nombre, String descripcion, String rareza, int value, int durabilidad,
            boolean obtenido, String contenido) {
    super(code, descripcion, rareza, value, durabilidad, nombre, obtenido);
    this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void leerCarta() {
        // Aquí puedes implementar la lógica correspondiente a la lectura de la carta
        System.out.println("Has leído una carta: " + contenido);
    }
    @Override
    public String toString() {
        return super.toString() + "%" + contenido;
    }
}
