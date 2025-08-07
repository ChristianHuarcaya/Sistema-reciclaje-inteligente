package com.reciclaje.inteligente.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reciclaje.inteligente.Entidad.Educacion;

public interface EducacionRepository extends JpaRepository<Educacion, Long> {

	List<Educacion> findByNivel(Integer nivel);
}
