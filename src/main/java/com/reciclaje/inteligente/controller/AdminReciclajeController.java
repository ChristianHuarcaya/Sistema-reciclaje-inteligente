package com.reciclaje.inteligente.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.reciclaje.inteligente.Entidad.Reciclaje;
import com.reciclaje.inteligente.Repository.ReciclajeRepository;

@Controller
@RequestMapping("/admin/reciclaje")
public class AdminReciclajeController {
	private final ReciclajeRepository reciclajeRepository;

    public AdminReciclajeController(ReciclajeRepository reciclajeRepository) {
        this.reciclajeRepository = reciclajeRepository;
    }

    
@GetMapping("/historial")
public String listarReciclajesFiltrados(
        @RequestParam(required = false) String usuario,
        @RequestParam(required = false) String estacion,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
        Model model) {

    List<Reciclaje> reciclajes = reciclajeRepository.findAll();

    if (usuario != null && !usuario.isEmpty()) {
        reciclajes = reciclajes.stream()
            .filter(r -> r.getUsuario().getNombre().toLowerCase().contains(usuario.toLowerCase()))
            .toList();
    }

    if (estacion != null && !estacion.isEmpty()) {
        reciclajes = reciclajes.stream()
            .filter(r -> r.getEstacion().getNombre().toLowerCase().contains(estacion.toLowerCase()))
            .toList();
    }

    if (desde != null) {
        reciclajes = reciclajes.stream()
            .filter(r -> !r.getFecha().toLocalDate().isBefore(desde))
            .toList();
    }

    if (hasta != null) {
        reciclajes = reciclajes.stream()
            .filter(r -> !r.getFecha().toLocalDate().isAfter(hasta))
            .toList();
    }

    model.addAttribute("reciclajes", reciclajes);
    return "admin/reciclaje";
}

}


