package me.malkon.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import me.malkon.cursomc.services.DBService;
import me.malkon.cursomc.services.EmailService;
import me.malkon.cursomc.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBService dbService;

	@Value("${spring.jpa.hibernate.ddl-auto}") // pega o valor da chave no properties
	private String strategy;

	/*
			 * Só instancia os dados se o valor da chave no application.properties for
			 * create
			 */
	@Bean 
	public boolean instantiateDatabase() throws ParseException {

		if (!"create".equals(strategy))
			return false;

		dbService.instantiateTestDatabase();

		return true;

	}

	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}

}
