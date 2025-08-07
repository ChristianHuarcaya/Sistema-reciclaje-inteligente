package com.reciclaje.inteligente.Entidad;

import java.util.List;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;

@Entity
public class Estacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;
	private String ubicacion;
	private String estado; // ACTIVA, MANTENIMIENTO, LLENA, etc.
	@Column(nullable = false)
	private double latitud;
	@Column(nullable = false)
	private double longitud;

	@OneToMany(mappedBy = "estacion")
	private List<Reciclaje> reciclajes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
		
		
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public List<Reciclaje> getReciclajes() {
		return reciclajes;
	}

	public void setReciclajes(List<Reciclaje> reciclajes) {
		this.reciclajes = reciclajes;
	}

}
