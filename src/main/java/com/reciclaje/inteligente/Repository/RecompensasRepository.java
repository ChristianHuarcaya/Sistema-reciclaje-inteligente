package com.reciclaje.inteligente.Repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.reciclaje.inteligente.Entidad.Recompensas;

public interface RecompensasRepository extends JpaRepository<Recompensas, Long> {

	 List<Recompensas> findByUsuarioId(Long usuarioId);
	
}
