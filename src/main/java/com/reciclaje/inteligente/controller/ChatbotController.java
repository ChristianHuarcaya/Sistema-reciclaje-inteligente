package com.reciclaje.inteligente.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reciclaje.inteligente.Entidad.ChatLog;
import com.reciclaje.inteligente.Entidad.Usuario;
import com.reciclaje.inteligente.Repository.ChatLogRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/chatbot")
public class ChatbotController {

	private final ChatLogRepository chatLogRepository;

	public ChatbotController(ChatLogRepository chatLogRepository) {
		this.chatLogRepository = chatLogRepository;

	}

	@GetMapping
	public String mostrarChat() {
		return "chatbot/chatbot"; // Vista del chatbot
	}

	@PostMapping("/enviar")
	@ResponseBody
	public Map<String, String> procesarPregunta(@RequestParam String mensaje, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

		if (usuario == null) {
			response.put("respuesta", "⚠️ Debes iniciar sesión para usar el chatbot.");
			return response;
		}

		// 🧠 Simulador de IA básico
		String respuestaBot = generarRespuestaSimulada(mensaje);

		// ✅ Guardamos la conversación
		ChatLog chatLog = new ChatLog();
		chatLog.setMensajeUsuario(mensaje);
		chatLog.setRespuestaBot(respuestaBot);
		chatLog.setFecha(LocalDateTime.now());
		chatLog.setUsuario(usuario);
		chatLogRepository.save(chatLog);

		response.put("respuesta", respuestaBot);
		return response;
	}

	private String generarRespuestaSimulada(String mensajeUsuario) {
		String msg = mensajeUsuario.toLowerCase();

		if (msg.contains("plastico") || msg.contains("plástico")) {
			return "🧴 El plástico es altamente reciclable. Recuerda enjuagar los envases antes de desecharlos.";
		} else if (msg.contains("papel") || msg.contains("cartón")) {
			return "📄 El papel limpio y seco puede reciclarse fácilmente. Evita mezclarlo con comida o grasa.";
		} else if (msg.contains("vidrio")) {
			return "🍾 El vidrio puede reciclarse infinitamente sin perder calidad. Separa colores si es posible.";
		} else if (msg.contains("hola")) {
			return "👋 ¡Hola! ¿Tienes alguna pregunta sobre reciclaje?";
		} else if (msg.contains("gracias")) {
			return "♻️ ¡De nada! Siempre listo para ayudarte con el reciclaje.";
		} else {
			return "🤖 Soy un bot de reciclaje recien estoy en proceso de entrenamiento para respoderte mejor. Pregúntame sobre plástico, papel, cartón o vidrio.";
		}
	}

}