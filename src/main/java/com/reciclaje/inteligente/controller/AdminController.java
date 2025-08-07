package com.reciclaje.inteligente.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.reciclaje.inteligente.Entidad.Reciclaje;
import com.reciclaje.inteligente.Entidad.Usuario;

import com.reciclaje.inteligente.Repository.ReciclajeRepository;

import com.reciclaje.inteligente.Repository.UsuarioRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ReciclajeRepository reciclajeRepository;

	@GetMapping("/dashboard")
	public String mostrarDashboardAdmin(Model model, Principal principal) {
		Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElse(null);
		model.addAttribute("usuario", usuario);

		model.addAttribute("reciclajesCount", reciclajeRepository.count());
		model.addAttribute("usuariosCount", usuarioRepository.count());
		model.addAttribute("recompensasCount", 0); // reemplaza con tu lógica real si tienes una tabla de recompensas

		return "admin/dashboard";
	}

	// 🧾 Listar usuarios
	@GetMapping("/usuarios")
	public String listarUsuarios(Model model) {
		List<Usuario> usuarios = usuarioRepository.findAll().stream().filter(u -> !"ADMIN".equals(u.getRol())) // Excluye
				.toList();

		model.addAttribute("usuarios", usuarios);
		return "admin/usuarios";
	}

	// 📝 Editar usuario
	@PostMapping("/usuarios/editar/{id}")
	public String actualizarUsuario(@PathVariable Long id, @ModelAttribute("usuario") Usuario usuarioActualizado) {
		Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido"));
		usuario.setNombre(usuarioActualizado.getNombre());
		usuario.setEmail(usuarioActualizado.getEmail());
		usuario.setRol(usuarioActualizado.getRol());
		usuarioRepository.save(usuario);
		return "redirect:/admin/usuarios";
	}

	// 🗑 Eliminar usuario
	@GetMapping("/usuarios/eliminar/{id}")
	public String eliminarUsuario(@PathVariable Long id) {
		usuarioRepository.deleteById(id);
		return "redirect:/admin/usuarios";
	}

	// para filtrar todo los reciclajes por usuario
	@GetMapping("/reciclajes")
	public String listarReciclajes(Model model) {
		List<Reciclaje> reciclajes = reciclajeRepository.findAllByOrderByFechaDesc();
		model.addAttribute("reciclajes", reciclajes);
		return "admin/reciclajes"; // Esta es la vista (archivo HTML) que se mostrará
	}

}
