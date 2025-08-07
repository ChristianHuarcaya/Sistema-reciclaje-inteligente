package com.reciclaje.inteligente.controller;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.reciclaje.inteligente.Entidad.DescuentoGeneral;

import com.reciclaje.inteligente.QR.QRUtil;
import com.reciclaje.inteligente.Repository.DescuentoGeneralRepository;

import com.reciclaje.inteligente.Services.EmailService;

@Controller
@RequestMapping("/usuarios/tickets")
public class TicketDescuentoController {

	private final DescuentoGeneralRepository descuentoGeneralRepo;
	private final EmailService emailService;

	public TicketDescuentoController(EmailService emailService, DescuentoGeneralRepository descuentoGeneralRepo) {
		this.emailService = emailService;
		this.descuentoGeneralRepo = descuentoGeneralRepo;
	}

	// 📧 Envía un ticket de descuento por correo con un código único y un QR.

	@PostMapping("/enviar")
	public String enviarDescuentoGeneral(@RequestParam("descuentoId") Long descuentoId, Principal principal,
			RedirectAttributes redirectAttrs) {

		String emailUsuario = principal.getName();

		Optional<DescuentoGeneral> optDesc = descuentoGeneralRepo.findById(descuentoId);

		if (optDesc.isEmpty()) {
			redirectAttrs.addFlashAttribute("error", "Descuento no encontrado.");
			return "redirect:/usuarios/beneficios?tab=reclamados";
		}

		DescuentoGeneral desc = optDesc.get();

		if (!desc.getUsuario().getEmail().equals(emailUsuario)) {
			redirectAttrs.addFlashAttribute("error", "No tienes permiso para este descuento.");
			return "redirect:/usuarios/beneficios?tab=reclamados";
		}

		try {
			// Generar código único si no existe aún
			if (desc.getCodigoUnico() == null || desc.getCodigoUnico().isEmpty()) {
				desc.setCodigoUnico(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			}

			// Generar imagen QR a partir del código
			byte[] qrImage = QRUtil.generateQRCodeImage(desc.getCodigoUnico(), 250, 250);

			// Construir el contenido del correo
			String asunto = "🎁 Tu Ticket de Descuento - Reciclaje Inteligente";

			String contenido = "<h3>¡Hola " + desc.getUsuario().getNombre() + "!</h3>"
					+ "<p>Obtuviste este ticket gracias a tu contribución en el reciclaje.</p>"
					+ "<p><strong>Descuento:</strong> " + desc.getPorcentaje() + "% en " + desc.getNombreBeneficio()
					+ " (" + desc.getCategoria() + ")</p>" + "<p><strong>Código único:</strong> "
					+ desc.getCodigoUnico() + "</p>" + "<p><img src='cid:qrCodigo' alt='Código QR'/></p>"
					+ "<p>¡Gracias por cuidar el planeta! 🌱</p>";

			emailService.enviarTicketConQR(emailUsuario, asunto, contenido, qrImage);

			redirectAttrs.addFlashAttribute("mensaje", "✅ Ticket enviado correctamente.");
		} catch (Exception e) {
			redirectAttrs.addFlashAttribute("error", "❌ Error al enviar correo: " + e.getMessage());
		}

		return "redirect:/usuarios/beneficios?tab=reclamados";
	}
}
