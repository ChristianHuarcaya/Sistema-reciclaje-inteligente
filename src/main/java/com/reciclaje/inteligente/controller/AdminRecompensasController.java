package com.reciclaje.inteligente.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.reciclaje.inteligente.Entidad.Recompensas;
import com.reciclaje.inteligente.Repository.RecompensasRepository;

@Controller
@RequestMapping("/admin/recompensas")
public class AdminRecompensasController {

	@Autowired
	private RecompensasRepository recompensasRepository;

	// 👉 Listar recompensas
	@GetMapping
	public String listarRecompensas(Model model) {
		model.addAttribute("recompensas", recompensasRepository.findAll());
		return "admin/recompensas"; // Debe estar en templates/admin/recompensas.html
	}

	// 👉 Mostrar formulario para crear nueva recompensa
	@GetMapping("/nueva")
	public String mostrarFormularioCrear(Model model) {
		model.addAttribute("recompensa", new Recompensas());
		return "admin/nueva-recompensa"; // Debe estar en templates/admin/nueva-recompensa.html
	}

	// 👉 Guardar nueva recompensa
	@PostMapping("/crear")
	public String crearRecompensa(@ModelAttribute("recompensa") Recompensas recompensa,
			RedirectAttributes redirectAttributes) {
		recompensa.setFechaCanje(null);
		recompensa.setUsuario(null);
		recompensasRepository.save(recompensa);
		redirectAttributes.addFlashAttribute("exito", "Recompensa creada exitosamente.");
		return "redirect:/admin/recompensas";
	}

	// 👉 Mostrar formulario para editar
	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
		Recompensas recompensa = recompensasRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("ID inválido"));
		model.addAttribute("recompensa", recompensa);
		return "admin/editar-recompensa"; // Debe estar en templates/admin/editar-recompensa.html
	}

	// 👉 Guardar edición de recompensa
	@PostMapping("/editar/{id}")
	public String editarRecompensa(@PathVariable Long id,
			@ModelAttribute("recompensa") Recompensas recompensaActualizada, RedirectAttributes redirectAttributes) {
		Recompensas recompensa = recompensasRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("ID inválido"));

		recompensa.setNombre(recompensaActualizada.getNombre());
		recompensa.setDescripcion(recompensaActualizada.getDescripcion());
		recompensa.setCostoPuntos(recompensaActualizada.getCostoPuntos());
		// recompensa.setImagen(recompensaActualizada.getImagen());

		recompensasRepository.save(recompensa);
		redirectAttributes.addFlashAttribute("exito", "Recompensa actualizada correctamente.");
		return "redirect:/admin/recompensas";
	}

	// 👉 Eliminar recompensa
	@GetMapping("/eliminar/{id}")
	public String eliminarRecompensa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		recompensasRepository.deleteById(id);
		redirectAttributes.addFlashAttribute("exito", "Recompensa eliminada.");
		return "redirect:/admin/recompensas";
	}

	@PostMapping("/guardar")
	public String guardarRecompensa(@ModelAttribute Recompensas recompensa,
			@RequestParam("imagenFile") MultipartFile imagenFile, RedirectAttributes redirectAttributes) {

		if (!imagenFile.isEmpty()) {
			String nombreImagen = imagenFile.getOriginalFilename();

			try {
				String ruta = "src/main/resources/static/images/recompensas/" + nombreImagen;
				imagenFile.transferTo(new java.io.File(ruta));
				recompensa.setImagen(nombreImagen);
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("error", "Error al subir la imagen.");
				return "redirect:/admin/recompensas";
			}
		}

		recompensasRepository.save(recompensa);
		redirectAttributes.addFlashAttribute("exito", "Recompensa creada exitosamente.");
		return "redirect:/admin/recompensas";
	}

}
