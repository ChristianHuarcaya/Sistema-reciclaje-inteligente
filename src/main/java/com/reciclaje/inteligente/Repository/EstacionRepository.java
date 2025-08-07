package com.reciclaje.inteligente.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reciclaje.inteligente.Entidad.Estacion;

public interface EstacionRepository extends JpaRepository<Estacion, Long> {

	// Buscar estaciones por estado
	List<Estacion> findByEstado(String estado);
}
