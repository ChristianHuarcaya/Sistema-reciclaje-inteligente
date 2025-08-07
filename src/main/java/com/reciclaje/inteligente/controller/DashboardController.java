package com.reciclaje.inteligente.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.reciclaje.inteligente.Entidad.Usuario;
import com.reciclaje.inteligente.Repository.ChatLogRepository;
import com.reciclaje.inteligente.Repository.EducacionRepository;
import com.reciclaje.inteligente.Repository.EstacionRepository;
import com.reciclaje.inteligente.Repository.ReciclajeRepository;
import com.reciclaje.inteligente.Repository.RecompensasRepository;
import com.reciclaje.inteligente.Repository.UsuarioRepository;

@Controller
public class DashboardController {

	private final UsuarioRepository usuarioRepository;
	private final EducacionRepository educacionRepository;
	private final EstacionRepository estacionRepository;
	private final RecompensasRepository recompensasRepository;
	private final ReciclajeRepository reciclajeRepository;
	private final ChatLogRepository chatlogRepository;

	public DashboardController(UsuarioRepository usuarioRepository, EducacionRepository educacionRepository,
			EstacionRepository estacionRepository, RecompensasRepository recompensasRepository,
			ReciclajeRepository reciclajeRepository, ChatLogRepository chatlogRepository) {
		this.usuarioRepository = usuarioRepository;
		this.educacionRepository = educacionRepository;
		this.estacionRepository = estacionRepository;
		this.recompensasRepository = recompensasRepository;
		this.reciclajeRepository = reciclajeRepository;
		this.chatlogRepository = chatlogRepository;
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		String email = principal.getName(); // ✅ Obtiene el email del usuario autenticado
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null); // ✅ .orElse(null) para resolver el
																				// Optional

		if (usuario != null && "ROLE_ADMIN".equals(usuario.getRol())) {
			model.addAttribute("usuariosTotal", usuarioRepository.count());
			model.addAttribute("educacionTotal", educacionRepository.count());
			model.addAttribute("estacionTotal", estacionRepository.count());
			model.addAttribute("recompensasTotal", recompensasRepository.count());
			model.addAttribute("reciclajeTotal", reciclajeRepository.count());
			model.addAttribute("chatlogTotal", chatlogRepository.count());

			model.addAttribute("usuario", usuario);

			return "admin/dashboard"; // Asegúrate de tener este HTML
		}

		return "redirect:/usuario/home"; // Vista para usuarios normales
	}
}
