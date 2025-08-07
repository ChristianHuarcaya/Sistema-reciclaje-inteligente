package com.reciclaje.inteligente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioHomeController {

	@GetMapping("/home")
	public String homeUsuario() {
		return "usuario/home"; // usuario/home.html
	}

}
