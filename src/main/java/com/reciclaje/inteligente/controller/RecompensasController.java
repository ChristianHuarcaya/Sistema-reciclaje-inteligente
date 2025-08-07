package com.reciclaje.inteligente.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.reciclaje.inteligente.Entidad.Recompensas;
import com.reciclaje.inteligente.Entidad.Usuario;
import com.reciclaje.inteligente.Repository.RecompensasRepository;
import com.reciclaje.inteligente.Repository.UsuarioRepository;

import java.util.List;

@Controller
@RequestMapping("/recompensas")
public class RecompensasController {

	private final RecompensasRepository recompensasRepository;
	private final UsuarioRepository usuarioRepository;

	public RecompensasController(RecompensasRepository recompensasRepository, UsuarioRepository usuarioRepository) {
		this.recompensasRepository = recompensasRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@GetMapping
	public String mostrarRecompensas(Model model, Authentication authentication) {
		String email = authentication.getName();
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		if (usuario != null) {
			model.addAttribute("usuario", usuario);
			model.addAttribute("recompensas", recompensasRepository.findAll());
		}
		return "usuarios/recompensas";

	}

	@PostMapping("/canjear/{recompensaId}")
	public String canjearRecompensa(@PathVariable Long recompensaId, Authentication authentication,
			RedirectAttributes redirectAttributes) {
		String email = authentication.getName();
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		Recompensas recompensa = recompensasRepository.findById(recompensaId).orElse(null);

		if (usuario == null || recompensa == null) {
			redirectAttributes.addFlashAttribute("error", "Usuario o recompensa no encontrados.");
			return "redirect:/recompensas";
		}

		if (usuario.getPuntos() < recompensa.getCostoPuntos()) {
			redirectAttributes.addFlashAttribute("error", "No tienes suficientes puntos para esta recompensa.");
			return "redirect:/recompensas";
		}

		// Descontar puntos y guardar
		usuario.setPuntos(usuario.getPuntos() - recompensa.getCostoPuntos());
		recompensa.setUsuario(usuario);
		recompensa.setFechaCanje(java.time.LocalDateTime.now());

		usuarioRepository.save(usuario);
		recompensasRepository.save(recompensa);

		redirectAttributes.addFlashAttribute("exito", "¡Recompensa canjeada exitosamente!");
		return "redirect:/recompensas";
	}
}
