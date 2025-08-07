package com.reciclaje.inteligente.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.reciclaje.inteligente.Services.UsuarioDetailsService;
import com.reciclaje.inteligente.security.CustomLoginSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final UsuarioDetailsService usuarioDetailsService;
	private final CustomLoginSuccessHandler loginSuccessHandler;

	public SecurityConfig(UsuarioDetailsService uds, CustomLoginSuccessHandler handler) {
		this.usuarioDetailsService = uds;
		this.loginSuccessHandler = handler;
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(usuarioDetailsService)
				.passwordEncoder(passwordEncoder()).and().build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth

				.requestMatchers("/", "/historial/", "/home", "/login", "/registro", "/usuarios/registro", "/css/**",
						"/js/**", "/imagenes/**")
				.permitAll()

				// Rutas protegidas por rol
				.requestMatchers("/admin/**", "/usuarios/lista").hasRole("ADMIN").requestMatchers("/usuarios/**")
				.hasRole("USER") // CORREGIDO: debe ser /usuarios/**

				.requestMatchers("/ticketdescuento/**",
						"/historial/**",
						"/reciclaje/**",
						"/estaciones/**",
						"/educacion/**", 
						"/recompensas/**",
						"/chatbot/**",
						"/usuarios/tickets/enviar",
						"/beneficios/", 
						"/usuarios/beneficios/reclamar**")

				.hasAnyRole("ADMIN", "USER")

				.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").usernameParameter("email").passwordParameter("password")
						.successHandler(loginSuccessHandler).permitAll())
				.logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());

		return http.build();
	}
}
