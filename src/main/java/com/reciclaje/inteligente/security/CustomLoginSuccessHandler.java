package com.reciclaje.inteligente.security;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.reciclaje.inteligente.Entidad.Usuario;
import com.reciclaje.inteligente.Repository.UsuarioRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;  // ✔️ Agregado

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication) throws IOException, ServletException {

        String email = authentication.getName();  // ✔️ Recupera el email del login

        // Busca al usuario completo en la base de datos
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        // Guarda el usuario en la sesión si existe
        if (usuario != null) {
            request.getSession().setAttribute("usuarioActual", usuario);
        }

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (roles.contains("ROLE_USER")) {
            response.sendRedirect("/usuarios/home");
        } else {
            response.sendRedirect("/acceso-denegado");
        }
    }
}
