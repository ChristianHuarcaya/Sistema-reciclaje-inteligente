package com.reciclaje.inteligente.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.reciclaje.inteligente.Entidad.DescuentoGeneral;
import com.reciclaje.inteligente.Entidad.Reciclaje;
import com.reciclaje.inteligente.Entidad.Usuario;
import com.reciclaje.inteligente.QR.QRUtil;
import com.reciclaje.inteligente.Repository.DescuentoGeneralRepository;
import com.reciclaje.inteligente.Repository.ReciclajeRepository;

import com.reciclaje.inteligente.Repository.UsuarioRepository;

@Service
public class ReciclajeService {

	private final ReciclajeRepository reciclajeRepo;
	private final UsuarioRepository usuarioRepo;
	private final DescuentoGeneralRepository descuentoGeneralRepo;

	private final EmailService emailService;

	@Autowired
	public ReciclajeService(ReciclajeRepository reciclajeRepo, UsuarioRepository usuarioRepo, EmailService emailService,
			DescuentoGeneralRepository descuentoGeneralRepo) {
		this.reciclajeRepo = reciclajeRepo;
		this.usuarioRepo = usuarioRepo;
		this.descuentoGeneralRepo = descuentoGeneralRepo;

		this.emailService = emailService;
	}

	public String registrarReciclaje(Usuario usuario, Long estacionId, String tipo, double peso) {

		Reciclaje reciclaje = new Reciclaje();
		reciclaje.setUsuario(usuario);
		reciclaje.setEstacion(null); // set estación si lo necesitas desde fuera
		reciclaje.setTipoMaterial(tipo);
		reciclaje.setPeso(peso);
		reciclaje.setFecha(LocalDateTime.now());
		reciclajeRepo.save(reciclaje);

		int puntosGanados = (int) (peso * 10);
		usuario.setPuntos(usuario.getPuntos() + puntosGanados);
		usuarioRepo.save(usuario);

		// Evaluar si genera ticket
		LocalDateTime ahora = LocalDateTime.now();
		int anio = ahora.getYear();
		int mes = ahora.getMonthValue();

		double pesoMes = reciclajeRepo.sumarPesoTotalPorUsuario(usuario.getId());
		boolean yaTieneTicket = descuentoGeneralRepo.existeTicketMes(usuario.getId(), anio, mes);

		if (pesoMes >= 100 && !yaTieneTicket) {
			DescuentoGeneral ticket = new DescuentoGeneral();
			ticket.setUsuario(usuario);
			ticket.setFechaGeneracion(LocalDate.now());
			ticket.setPorcentaje(10);
			// * Código único del ticket

			String codigoUnico = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
			ticket.setCodigoUnico(codigoUnico);

			descuentoGeneralRepo.save(ticket);

			try {
				byte[] qrImage = QRUtil.generateQRCodeImage(codigoUnico, 250, 250);
				String asunto = "🎫 Ticket con QR - Reciclaje Inteligente";
				String contenido = "<h3>¡Hola " + usuario.getNombre() + "!</h3>"
						+ "<p>Has generado un nuevo ticket de descuento. Código único:</p>" + "<p><strong>"
						+ codigoUnico + "</strong></p>" + "<p>Escanea el siguiente código QR para usarlo:</p>"
						+ "<img src='cid:qrCodigo' alt='Código QR' />";

				emailService.enviarTicketConQR(usuario.getEmail(), asunto, contenido, qrImage);
			} catch (Exception e) {
				return "🎟️ Ticket generado, pero falló el envío por correo: " + e.getMessage();
			}

			return "🎟️ ¡Ticket generado y enviado por correo!";
		}

		return "¡Reciclaje registrado con éxito! Ganaste " + puntosGanados + " puntos.";
	}
}
