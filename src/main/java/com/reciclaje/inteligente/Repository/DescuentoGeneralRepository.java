package com.reciclaje.inteligente.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reciclaje.inteligente.Entidad.DescuentoGeneral;

@Repository
public interface DescuentoGeneralRepository extends JpaRepository<DescuentoGeneral,Long>{
	
	List<DescuentoGeneral> findByUsuarioEmail(String email);
    List<DescuentoGeneral> findByUsuarioId(Long id);
    
    @Query("SELECT COUNT(d) > 0 FROM DescuentoGeneral d WHERE d.usuario.id = :usuarioId AND FUNCTION('YEAR', d.fechaGeneracion) = :anio AND FUNCTION('MONTH', d.fechaGeneracion) = :mes")
    boolean existeTicketMes(@Param("usuarioId") Long usuarioId, @Param("anio") int anio, @Param("mes") int mes);

    List<DescuentoGeneral> findByUsuarioIsNull();

}
