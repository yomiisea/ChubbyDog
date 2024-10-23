package com.mygdx.game.items;

public class Item {
	String nombre, descripcion, rareza;
	int value, durabilidad, code;
	boolean obtenido;
	public Item(int code, String descripcion, String rareza, int value, int durabilidad, String nombre,
			boolean obtenido) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.rareza = rareza;
		this.value = value;
		this.durabilidad = durabilidad;
		this.code = code;
		this.obtenido = obtenido;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getRareza() {
		return rareza;
	}
	public void setRareza(String rareza) {
		this.rareza = rareza;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getDurabilidad() {
		return durabilidad;
	}
	public void setDurabilidad(int durabilidad) {
		this.durabilidad = durabilidad;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public boolean isObtenido() {
		return obtenido;
	}
	public void setObtenido(boolean obtenido) {
		this.obtenido = obtenido;
	}
	 @Override
	    public String toString() {
	        return code + "%" + nombre + "%" + descripcion + "%" + rareza + "%" + value + "%" + durabilidad + "%" + obtenido;
	    }
	public void usar() {
		
	}
	public void descartar() {
		
	}
	
}
