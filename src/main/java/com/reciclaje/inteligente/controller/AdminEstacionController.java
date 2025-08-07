package com.reciclaje.inteligente.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.reciclaje.inteligente.Entidad.Estacion;
import com.reciclaje.inteligente.Repository.EstacionRepository;

@Controller
@RequestMapping("/admin/estaciones")
public class AdminEstacionController {
	
	@Autowired
    private EstacionRepository estacionRepository;

    @GetMapping
    public String listarEstaciones(Model model) {
        model.addAttribute("estaciones", estacionRepository.findAll());
        return "admin/estaciones";
    }

    @GetMapping("/editar/{id}")
    public String editarEstacion(@PathVariable Long id, Model model) {
        Estacion estacion = estacionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID inválido"));
        model.addAttribute("estacion", estacion);
        return "admin/editar-estacion";
    }

    @PostMapping("/editar/{id}")
    public String actualizarEstacion(@PathVariable Long id, @ModelAttribute("estacion") Estacion estacionActualizada) {
        Estacion estacion = estacionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID inválido"));

        estacion.setNombre(estacionActualizada.getNombre());
        estacion.setUbicacion(estacionActualizada.getUbicacion());
        estacion.setEstado(estacionActualizada.getEstado());
        estacionRepository.save(estacion);

        return "redirect:/admin/estaciones";
    }

    @GetMapping("/toggle/{id}")
    public String cambiarEstadoEstacion(@PathVariable Long id) {
        Estacion estacion = estacionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID inválido"));
        estacion.setEstado(estacion.getEstado().equals("Activo") ? "Inactivo" : "Activo");
        estacionRepository.save(estacion);
        return "redirect:/admin/estaciones";
    }
}


