package com.reciclaje.inteligente.Services;

import com.reciclaje.inteligente.Entidad.Usuario;
import com.reciclaje.inteligente.Repository.UsuarioRepository;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// System.out.println("🔐 Intentando login con email: " + email);

		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

		// System.out.println("✅ Usuario encontrado: " + usuario.getNombre() + " | Rol:
		// " + usuario.getRol());

		// Convertimos el rol en una lista de autoridades
		return new org.springframework.security.core.userdetails.User(usuario.getEmail(), usuario.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol())));
	}
}
