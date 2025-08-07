package com.reciclaje.inteligente.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.reciclaje.inteligente.Entidad.DescuentoGeneral;
import com.reciclaje.inteligente.Entidad.Estacion;
import com.reciclaje.inteligente.Entidad.Reciclaje;

import com.reciclaje.inteligente.Entidad.Usuario;

import com.reciclaje.inteligente.Repository.DescuentoGeneralRepository;
import com.reciclaje.inteligente.Repository.EstacionRepository;
import com.reciclaje.inteligente.Repository.ReciclajeRepository;

import com.reciclaje.inteligente.Repository.UsuarioRepository;
import com.reciclaje.inteligente.Services.ReciclajeService;
import com.reciclaje.inteligente.Services.UsuarioDetailsService;

@Controller
@RequestMapping("/reciclaje")
public class ReciclajeController {

	private final ReciclajeRepository reciclajeRepo;
	private final UsuarioRepository usuarioRepo;
	private final EstacionRepository estacionRepo;
	
	private final DescuentoGeneralRepository descuentoGeneralRepo;

	@Autowired
	public ReciclajeController(ReciclajeRepository reciclajeRepo,
			UsuarioRepository usuarioRepo, EstacionRepository estacionRepo,
			 DescuentoGeneralRepository descuentoGeneralRepo) {
		this.reciclajeRepo = reciclajeRepo;
		this.usuarioRepo = usuarioRepo;
		this.estacionRepo = estacionRepo;
		
		this.descuentoGeneralRepo = descuentoGeneralRepo;

	}

	// 📥 REGISTRO DE RECICLAJE
	@PostMapping("/registrar")

	public String registrar(@RequestParam Long estacionId, @RequestParam String tipo, @RequestParam double peso,
			Principal principal, Model model) {
		Usuario usuario = usuarioRepo.findByEmail(principal.getName()).orElse(null);
		Estacion estacion = estacionRepo.findById(estacionId).orElse(null);

		if (usuario == null || estacion == null) {
			model.addAttribute("error", "Usuario o estación no encontrados.");
			model.addAttribute("estaciones", estacionRepo.findAll());
			return "usuarios/reciclaje";
		}

		// Registrar reciclaje
		Reciclaje reciclaje = new Reciclaje();
		reciclaje.setUsuario(usuario);
		reciclaje.setEstacion(estacion);
		reciclaje.setTipoMaterial(tipo);
		reciclaje.setPeso(peso);
		reciclaje.setFecha(LocalDateTime.now());
		reciclajeRepo.save(reciclaje);

		// Actualizar puntos
		int puntosGanados = (int) (peso * 10);
		usuario.setPuntos(usuario.getPuntos() + puntosGanados);
		usuarioRepo.save(usuario);

		// Verificar total de reciclaje acumulado
		List<Reciclaje> reciclajesUsuario = reciclajeRepo.findByUsuarioId(usuario.getId());
		double totalKg = reciclajesUsuario.stream().mapToDouble(Reciclaje::getPeso).sum();

		if (totalKg >= 100) {
			DescuentoGeneral descuento = new DescuentoGeneral();
			descuento.setUsuario(usuario);
			descuento.setNombreBeneficio("Educación - UTP");
			descuento.setCategoria("educacion");
			descuento.setPorcentaje(10);
			descuento.setFechaGeneracion(LocalDate.now());
			

			descuentoGeneralRepo.save(descuento);

			model.addAttribute("descuentoGenerado", true);
			model.addAttribute("porcentaje", descuento.getPorcentaje());
		}

		model.addAttribute("mensaje", "¡Reciclaje registrado con éxito! Ganaste " + puntosGanados + " puntos.");
		model.addAttribute("estaciones", estacionRepo.findAll());
		return "usuarios/reciclaje"; // 🔁 return final seguro
	}

	// 📃 LISTAR TODOS LOS RECICLAJES
	@GetMapping("/todos")
	public List<Reciclaje> listarTodos() {
		return reciclajeRepo.findAll();
	}

	// 📃 LISTAR RECICLAJES DE UN USUARIO
	@GetMapping("/usuario/{id}")
	public List<Reciclaje> porUsuario(@PathVariable Long id) {
		return reciclajeRepo.findByUsuarioId(id);
	}

	// 🖥️ MOSTRAR FORMULARIO DE REGISTRO DE RECICLAJE
	@GetMapping("")
	public String mostrarVistaReciclaje(Model model) {
		model.addAttribute("estaciones", estacionRepo.findAll());
		return "usuarios/reciclaje";
	}

}
