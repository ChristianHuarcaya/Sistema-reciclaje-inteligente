package com.reciclaje.inteligente.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reciclaje.inteligente.Entidad.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	
	Optional<Usuario> findByNombre(String nombre);
	//Optional<Usuario> findByRol(String rol); // Cambiar esto por un Optional

	Optional<Usuario> findByEmail(String email); // ✔️ Usado para login

	Optional<Usuario> findByRol(String rol); // ✔️ Para listar usuarios por rol (ADMIN o USER)

	long count(); // ✔️ Para estadísticas (cantidad de usuarios)
}
