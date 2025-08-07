package com.reciclaje.inteligente.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.reciclaje.inteligente.Entidad.Educacion;
import com.reciclaje.inteligente.Repository.EducacionRepository;

@Controller
@RequestMapping("/admin/educacion")
public class AdminEducacionController {

    private final EducacionRepository educacionRepository;

    public AdminEducacionController(EducacionRepository educacionRepository) {
        this.educacionRepository = educacionRepository;
    }

    @GetMapping
    public String listarPreguntasAdmin(Model model) {
        List<Educacion> preguntas = educacionRepository.findAll();
        model.addAttribute("preguntas", preguntas);
        return "admin/educacion";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("pregunta", new Educacion());
        return "admin/crear-educacion";
    }

    @PostMapping("/crear")
    public String guardarPregunta(@ModelAttribute("pregunta") Educacion pregunta) {
        educacionRepository.save(pregunta);
        return "redirect:/admin/educacion";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Educacion pregunta = educacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido"));
        model.addAttribute("pregunta", pregunta);
        return "admin/editar-educacion";
    }

    @PostMapping("/editar/{id}")
    public String actualizarPregunta(@PathVariable Long id, @ModelAttribute("pregunta") Educacion actualizada) {
        Educacion existente = educacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido"));

        existente.setTextoPregunta(actualizada.getTextoPregunta());
        existente.setRespuestaCorrecta(actualizada.getRespuestaCorrecta());
        existente.setOpcionA(actualizada.getOpcionA());
        existente.setOpcionB(actualizada.getOpcionB());
        existente.setOpcionC(actualizada.getOpcionC());
        existente.setNivel(actualizada.getNivel());

        educacionRepository.save(existente);
        return "redirect:/admin/educacion";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPregunta(@PathVariable Long id) {
        educacionRepository.deleteById(id);
        return "redirect:/admin/educacion";
    }
}