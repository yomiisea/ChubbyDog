package com.mygdx.game.items;

public class Pocion extends Item {
    private int aumentoPoder;

    public Pocion(int code, String nombre, String descripcion, String rareza, int value, int durabilidad,
            boolean obtenido, int aumentoPoder) {
   super(code, descripcion, rareza, value, durabilidad, nombre, obtenido);
   this.aumentoPoder = aumentoPoder;
    }

    public int getAumentoPoder() {
        return aumentoPoder;
    }

    public void setAumentoPoder(int aumentoPoder) {
        this.aumentoPoder = aumentoPoder;
    }

    public void usarPocion() {
        // Aquí puedes implementar la lógica correspondiente al usar la poción
        System.out.println("Has usado una poción. Tu poder aumenta en " + aumentoPoder);
    }
    @Override
    public String toString() {
        return super.toString() + "%" + aumentoPoder;
    }
}
