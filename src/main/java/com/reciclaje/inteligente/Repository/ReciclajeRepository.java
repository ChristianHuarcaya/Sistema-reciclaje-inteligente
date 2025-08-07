package com.reciclaje.inteligente.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.reciclaje.inteligente.Entidad.ChatLog;
import com.reciclaje.inteligente.Entidad.Reciclaje;

public interface ReciclajeRepository extends JpaRepository<Reciclaje, Long> {
	List<Reciclaje> findByUsuarioId(Long usuarioId);

	List<Reciclaje> findByEstacionId(Long estacionId);

	List<Reciclaje> findAllByOrderByFechaDesc();

	@Query("SELECT COALESCE(SUM(r.peso), 0) FROM Reciclaje r WHERE r.usuario.id = :usuarioId")
	double sumarPesoTotalPorUsuario(@Param("usuarioId") Long usuarioId);

	long count();
}
