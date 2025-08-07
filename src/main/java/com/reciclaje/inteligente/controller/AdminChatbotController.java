package com.reciclaje.inteligente.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.reciclaje.inteligente.Entidad.ChatLog;
import com.reciclaje.inteligente.Repository.ChatLogRepository;

@Controller
@RequestMapping("/admin/chatbot")
public class AdminChatbotController {

	private final ChatLogRepository chatlogRepository;

	public AdminChatbotController(ChatLogRepository chatlogRepository) {
		this.chatlogRepository = chatlogRepository;
	}

	// Mostrar historial completo del chatbot para admin /historial
	@GetMapping("/historial")
	public String verHistorialCompleto(Model model) {
		List<ChatLog> historial = chatlogRepository.findAllByOrderByFechaDesc();
		model.addAttribute("chatlogs", historial);
		return "admin/chatbot-historial"; // Vista que muestra todos los chats
	}

	// Ver historial de un usuario específico
	@GetMapping("/historial/{usuarioId}")
	public String mostrarHistorialPorUsuario(@PathVariable Long usuarioId, Model model) {
		List<ChatLog> historial = chatlogRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
		model.addAttribute("chatlogs", historial);
		return "admin/chatbot-historial";
	}

	// 📝 Mostrar todos los chats ordenados por fecha
	@GetMapping
	public String verHistorialGeneral(Model model) {
		List<ChatLog> chatLogs = chatlogRepository.findAllByOrderByFechaDesc();
		model.addAttribute("chatlogs", chatLogs);
		return "admin/chatbot"; // ➡️ Tu HTML aquí
	}
}
