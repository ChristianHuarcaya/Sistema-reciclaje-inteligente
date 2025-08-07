package com.reciclaje.inteligente.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import com.reciclaje.inteligente.Entidad.Educacion;
import com.reciclaje.inteligente.Repository.EducacionRepository;

@Controller
@RequestMapping("/usuarios")
public class EducacionController {

	private final EducacionRepository educacionRepository;

	public EducacionController(EducacionRepository educacionRepository) {
		this.educacionRepository = educacionRepository;
	}

	@GetMapping("/educacion")
	public String mostrarPreguntaEducacion(Model model) {
		List<Educacion> preguntas = educacionRepository.findAll();

		// 👉 Crear lista de opciones para cada pregunta
		List<Map<String, Object>> preguntasConOpciones = new ArrayList<>();

		for (Educacion pregunta : preguntas) {
			Map<String, Object> preguntaMap = new HashMap<>();
			preguntaMap.put("id", pregunta.getId());
			// preguntas para insertar tipo texto
			preguntaMap.put("pregunta", pregunta.getTextoPregunta());

			preguntaMap.put("respuestaCorrecta", pregunta.getRespuestaCorrecta());

			List<String> opciones = Arrays.asList(pregunta.getOpcionA(), pregunta.getOpcionB(), pregunta.getOpcionC());
			preguntaMap.put("opciones", opciones);

			preguntasConOpciones.add(preguntaMap);
		}
		// System.out.println("✅ Cantidad de preguntas encontradas: " +
		// preguntas.size());
		model.addAttribute("preguntas", preguntasConOpciones);
		return "usuarios/educacion";
	}
}
