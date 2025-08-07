package com.reciclaje.inteligente.Entidad;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class DescuentoGeneral {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Usuario usuario;

	private String nombreBeneficio; // Ej: "IDAT", "Plaza Vea", etc.
	private String categoria; // Ej: "educacion", "alimentacion", "tributo"
	private int porcentaje; // % de descuento
	private String codigoUnico;
	private LocalDate fechaGeneracion;
	private boolean usado = false;

	// Constructor vacío obligatorio para JPA
	public DescuentoGeneral() {
	}

	// Constructor completo (puedes dejarlo si lo usas en otro lado)
	public DescuentoGeneral(Long id, Usuario usuario, String nombreBeneficio, String categoria, int porcentaje,
			String codigoUnico, LocalDate fechaGeneracion, boolean usado) {
		this.id = id;
		this.usuario = usuario;
		this.nombreBeneficio = nombreBeneficio;
		this.categoria = categoria;
		this.porcentaje = porcentaje;
		this.codigoUnico = codigoUnico;
		this.fechaGeneracion = fechaGeneracion;
		this.usado = usado;
	}

	// ✅ Constructor adicional para insertar datos iniciales
	public DescuentoGeneral(String nombreBeneficio, String categoria, int porcentaje) {
		this.nombreBeneficio = nombreBeneficio;
		this.categoria = categoria;
		this.porcentaje = porcentaje;
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNombreBeneficio() {
		return nombreBeneficio;
	}

	public void setNombreBeneficio(String nombreBeneficio) {
		this.nombreBeneficio = nombreBeneficio;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public int getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(int porcentaje) {
		this.porcentaje = porcentaje;
	}

	public String getCodigoUnico() {
		return codigoUnico;
	}

	public void setCodigoUnico(String codigoUnico) {
		this.codigoUnico = codigoUnico;
	}

	public LocalDate getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(LocalDate fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public boolean isUsado() {
		return usado;
	}

	public void setUsado(boolean usado) {
		this.usado = usado;
	}

}