package com.reciclaje.inteligente.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import com.reciclaje.inteligente.Entidad.Estacion;
import com.reciclaje.inteligente.Repository.EstacionRepository;

@Controller
@RequestMapping("/estaciones")
public class EstacionIoTController {
	private final EstacionRepository estacionRepository;

	public EstacionIoTController(EstacionRepository estacionRepository) {
		this.estacionRepository = estacionRepository;
	}

	// 🌍 Ver estaciones (solo lectura)
	@GetMapping("")
	public String mostrarEstacionesUsuario(Model model) {
		List<Estacion> estaciones = estacionRepository.findAll();
		model.addAttribute("estaciones", estaciones);
		return "estaciones/estaciones"; // Este es el HTML que verá el usuario
	}

	// 🗺 Ver mapa (opcional)
	@GetMapping("/mapa")
	public String mostrarMapaUsuario(Model model) {
		List<Estacion> estaciones = estacionRepository.findAll();
		model.addAttribute("estaciones", estaciones);
		return "usuario/mapa"; // Otro HTML si quieres mostrar en un mapa
	}

}
