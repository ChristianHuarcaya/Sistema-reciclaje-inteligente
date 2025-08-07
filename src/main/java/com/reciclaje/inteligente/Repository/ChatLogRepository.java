package com.reciclaje.inteligente.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reciclaje.inteligente.Entidad.ChatLog;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

	// Obtener logs por usuario
	List<ChatLog> findByUsuarioId(Long usuarioId);

	// 🕒 Buscar últimos 10 mensajes por usuario
	List<ChatLog> findTop10ByUsuarioIdOrderByFechaDesc(Long usuarioId);

	// 🕓 Buscar todos los logs ordenados por fecha descendente
	List<ChatLog> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

	// ✅ Todos los chats de todos los usuarios ordenados (NUEVO)
	List<ChatLog> findAllByOrderByFechaDesc();
	
	List<ChatLog> findByMensajeUsuarioContainingIgnoreCase(String texto);

}
