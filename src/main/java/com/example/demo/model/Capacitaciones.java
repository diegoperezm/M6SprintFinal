package com.example.demo.model;

public class Capacitaciones {

	private String nombre;
	private String detalle;

	public Capacitaciones() {}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public Capacitaciones( String nombre, String detalle) {
		super();
		//this.id = id;
		this.nombre = nombre;
		this.detalle = detalle;
	}


	@Override
	public String toString() {
		return "Capacitaciones nombre=" + nombre + ", detalle=" + detalle + "]";
	}

}