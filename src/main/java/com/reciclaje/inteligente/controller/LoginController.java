package com.reciclaje.inteligente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	// Muestra la vista de login cuando se accede a /login
	@GetMapping("/login")
	public String mostrarFormularioLogin() {
		return "login"; // Esto carga login.html
	}

	// Muestra la vista principal después del login
	@GetMapping("/inicio")
	public String vistaInicio() {
		return "inicio"; // llamado inicio.html
	}

	// Opcional: página de acceso denegado
	@GetMapping("/acceso-denegado")
	public String accesoDenegado() {
		return "acceso-denegado"; // acceso-denegado.html
	}
}
