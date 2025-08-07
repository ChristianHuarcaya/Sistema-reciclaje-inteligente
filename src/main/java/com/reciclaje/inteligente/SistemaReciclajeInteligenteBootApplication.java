package com.reciclaje.inteligente;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.reciclaje.inteligente.Entidad.DescuentoGeneral;

import com.reciclaje.inteligente.Repository.DescuentoGeneralRepository;

@SpringBootApplication
public class SistemaReciclajeInteligenteBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaReciclajeInteligenteBootApplication.class, args);
	}

	@Bean
	CommandLineRunner initDescuentosEducativos(DescuentoGeneralRepository repo) {
		return args -> {
			if (repo.count() == 0) {
				repo.save(new DescuentoGeneral("IDAT", "instituto", 10));
                repo.save(new DescuentoGeneral("Cibertec", "instituto", 15));
				repo.save(new DescuentoGeneral("Sise", "instituto", 12));
				repo.save(new DescuentoGeneral("UTP", "universidad", 10));
				repo.save(new DescuentoGeneral("UPC", "universidad", 15));
				repo.save(new DescuentoGeneral("Científica del Sur", "universidad", 20));
				repo.save(new DescuentoGeneral("San Ignacio", "universidad", 15));
				repo.save(new DescuentoGeneral("Autónoma", "universidad", 12));
				repo.save(new DescuentoGeneral("Británico", "idiomas", 20));
				repo.save(new DescuentoGeneral("ICPNA", "idiomas", 18));
			}
		};
	}

}
