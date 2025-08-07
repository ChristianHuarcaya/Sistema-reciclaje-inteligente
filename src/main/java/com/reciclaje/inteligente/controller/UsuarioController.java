package com.reciclaje.inteligente.controller;

import com.reciclaje.inteligente.Entidad.DescuentoGeneral;
import com.reciclaje.inteligente.Entidad.Reciclaje;
import com.reciclaje.inteligente.Entidad.Usuario;
import com.reciclaje.inteligente.QR.QRUtil;
import com.reciclaje.inteligente.Repository.DescuentoGeneralRepository;
import com.reciclaje.inteligente.Repository.ReciclajeRepository;
import com.reciclaje.inteligente.Repository.UsuarioRepository;
import com.reciclaje.inteligente.Services.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final ReciclajeRepository reciclajeRepository;

	@Autowired
	private DescuentoGeneralRepository descuentoGeneralRepo;

	private final EmailService emailService;

	@Autowired
	public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
			ReciclajeRepository reciclajeRepository, DescuentoGeneralRepository desGeneralRepository,
			EmailService emailService) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.reciclajeRepository = reciclajeRepository;
		this.descuentoGeneralRepo = desGeneralRepository;
		this.emailService = emailService;
	}

	// Muestra el formulario de registro
	@GetMapping("/registro")
	public String mostrarFormularioRegistro(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "registro";
	}

	// Registro de nuevos usuarios (USER o ADMIN)
	@PostMapping("/registro")
	public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
		// Verificar si el correo ya está registrado
		Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
		if (existente.isPresent()) {
			model.addAttribute("error", "El correo electrónico ya está registrado.");
			return "registro";
		}

		// Si el correo contiene "admin", verificar que no haya otro admin
		if (usuario.getEmail().contains("admin")) {
			Optional<Usuario> adminExistente = usuarioRepository.findByRol("ADMIN");
			if (adminExistente.isPresent()) {
				model.addAttribute("error", "Ya existe un usuario con el rol ADMIN.");
				return "registro";
			}
			usuario.setRol("ADMIN");
		} else {
			usuario.setRol("USER");
		}

		// Codificar contraseña y guardar usuario
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		usuarioRepository.save(usuario);

		return "redirect:/login?registro=exito";
	}

	// Vista del home del usuario luego de iniciar sesión
	@GetMapping("/home")
	public String homeUsuario(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepository.findByEmail(auth.getName()).orElse(null);
		model.addAttribute("usuario", usuario);
		return "usuarios/home";
	}

	// 📜 Vista del historial de reciclaje del usuario logueado
	@GetMapping("/historial")
	public String mostrarHistorialUsuario(Model model, Principal principal) {
		Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElse(null);

		if (usuario == null) {
			model.addAttribute("error", "No se encontró al usuario.");
			return "usuarios/historial";
		}

		// Lista de reciclajes del usuario
		List<Reciclaje> reciclajes = reciclajeRepository.findByUsuarioId(usuario.getId());
		model.addAttribute("reciclajes", reciclajes);

		// Total de kilos reciclados
		double totalKilos = reciclajes.stream().mapToDouble(Reciclaje::getPeso).sum();
		model.addAttribute("totalKilos", totalKilos);

		return "usuarios/historial";
	}

	// 🧾 Vista con pestañas: Beneficios disponibles y ya reclamados
	@GetMapping("beneficios")

	public String mostrarTodosLosBeneficios(Principal principal, Model model) {
		String email = principal.getName();
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

		double pesosTotales = reciclajeRepository.sumarPesoTotalPorUsuario(usuario.getId());

		List<DescuentoGeneral> disponibles = new ArrayList<>();

		if (pesosTotales >= 100) {
			// Solo si tiene 100kg o más mostramos los disponibles
			disponibles = descuentoGeneralRepo.findByUsuarioIsNull();
		}

		List<DescuentoGeneral> reclamados = descuentoGeneralRepo.findByUsuarioEmail(email);

		model.addAttribute("disponibles", disponibles);
		model.addAttribute("reclamados", reclamados);
		model.addAttribute("usuario", usuario);
		model.addAttribute("pesosTotales", pesosTotales); // por si quieres mostrarle cuánto lleva

		return "usuarios/beneficios";
	}

	// 🛍️ Reclamar beneficio disponible (lo asocia al usuario actual)
	@PostMapping("/beneficios/reclamar")
	public String reclamarBeneficio(@RequestParam Long idDescuento, Principal principal,
			RedirectAttributes redirectAttrs) {

		String email = principal.getName();
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

		if (usuario == null) {
			redirectAttrs.addFlashAttribute("error", "Usuario no encontrado.");
			return "redirect:/usuarios/beneficios";
		}

		Optional<DescuentoGeneral> opt = descuentoGeneralRepo.findById(idDescuento);
		if (opt.isEmpty() || opt.get().getUsuario() != null) {
			redirectAttrs.addFlashAttribute("error", "El beneficio ya fue reclamado o no existe.");
			return "redirect:/usuarios/beneficios";
		}

		DescuentoGeneral beneficio = opt.get();
		beneficio.setUsuario(usuario);
		beneficio.setFechaGeneracion(LocalDate.now());
		beneficio.setCodigoUnico(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

		descuentoGeneralRepo.save(beneficio);

		redirectAttrs.addFlashAttribute("mensaje",
				"🎟️ Beneficio reclamado. Puedes enviarlo por correo desde tu panel de tickets.");
		return "redirect:/usuarios/beneficios";
	}

}
